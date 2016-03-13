package com.gaia.hermes.tasks;

import java.util.UUID;

import com.gaia.hermes.bean.ApplicationBean;
import com.gaia.hermes.jobs.CreateApplicationJob;
import com.gaia.hermes.model.ApplicationModel;
import com.gaia.hermes.statics.Field;
import com.nhb.common.data.MapTuple;
import com.nhb.common.data.PuObject;
import com.nhb.common.db.sql.resp.SqlUpdateResponse;
import com.nhb.common.transaction.impl.AbstractDistributedTransactionTask;

public class CreateApplicationTask extends AbstractDistributedTransactionTask {
	private ApplicationModel model;
	private ApplicationBean application;

	public CreateApplicationTask(ApplicationModel model) {
		this.model = model;
	}

	@Override
	public void execute(PuObject data) {
		try {
			ApplicationBean bean = new ApplicationBean();
			bean.autoId();
			bean.setTimestamp(System.currentTimeMillis() / 1000);
			bean.setSecretKey(UUID.randomUUID().toString());

			if (data.variableExists(Field.BUNDLE_ID)) {
				bean.setBundleId(data.getString(Field.BUNDLE_ID));
			}

			if (data.variableExists(Field.NAME)) {
				bean.setName(data.getString(Field.NAME));
			}

			if (this.getJob() instanceof CreateApplicationJob) {
				CreateApplicationJob job = (CreateApplicationJob) getJob();
				bean.setPushNotificationConfigId(job.getCreatePushNotificationConfigTask().getBean().getId());
			}

			SqlUpdateResponse response = model.insert(bean);
			if (response.isSuccess()) {
				this.application = bean;
				setSuccessful(true);
			} else {
				setFailureDetails(PuObject.fromObject(
						new MapTuple<>(Field.EX, "create application error " + response.getException().getMessage())));
				throw new RuntimeException(response.getException());
			}
		} catch (Exception ex) {
			setFailureDetails(PuObject.fromObject(new MapTuple<>(Field.EX, "create application error")));
			throw new RuntimeException(ex);
		}
	}

	@Override
	public void rollback() {
		this.model.delete(this.application.getId());
	}

	public ApplicationBean getApplication() {
		return application;
	}

	public void setApplication(ApplicationBean application) {
		this.application = application;
	}

}
