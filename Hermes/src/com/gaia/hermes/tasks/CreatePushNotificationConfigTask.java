package com.gaia.hermes.tasks;

import java.util.UUID;

import com.gaia.hermes.bean.PushNotificationConfigBean;
import com.gaia.hermes.jobs.CreateApplicationJob;
import com.gaia.hermes.model.PushNotificationConfigModel;
import com.gaia.hermes.statics.Field;
import com.nhb.common.data.MapTuple;
import com.nhb.common.data.PuObject;
import com.nhb.common.db.sql.resp.SqlUpdateResponse;
import com.nhb.common.transaction.impl.AbstractDistributedTransactionTask;

public class CreatePushNotificationConfigTask extends AbstractDistributedTransactionTask {
	private PushNotificationConfigModel model;
	private PushNotificationConfigBean bean;

	public CreatePushNotificationConfigTask(PushNotificationConfigModel model) {
		this.model = model;
	}

	@Override
	public void execute(PuObject data) {
		try {
			PushNotificationConfigBean bean = new PushNotificationConfigBean();
			bean.setId(UUID.randomUUID());
			if (this.getJob() instanceof CreateApplicationJob) {
				CreateApplicationJob job = (CreateApplicationJob) this.getJob();
				bean.setApnsCertificateId(job.getCreateApnsCertificateTask().getApnsCertificate().getId());
				bean.setGcmKeyId(job.getCreateGcmKeyTask().getGcmKey().getId());
			}
			SqlUpdateResponse response = model.insert(bean);
			if (response.isSuccess()) {
				setSuccessful(true);
				this.setBean(bean);
			} else {
				setFailureDetails(PuObject.fromObject(new MapTuple<>(Field.EX,
						"creating push notification config is failure, error " + response.getException().getMessage())));
				throw new RuntimeException(response.getException());
			}
		} catch (Exception ex) {
			setFailureDetails(PuObject.fromObject(new MapTuple<>(Field.EX, "creating push notification config is failure")));
			throw new RuntimeException(ex);
		}
	}

	@Override
	public void rollback() {
		this.model.delete(this.bean.getId());
	}

	public PushNotificationConfigBean getBean() {
		return bean;
	}

	private void setBean(PushNotificationConfigBean bean) {
		this.bean = bean;
	}

}
