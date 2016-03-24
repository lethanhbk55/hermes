package com.gaia.hermes.async.tasks;

import java.util.UUID;

import com.fullmoon.task.AbstractTask;
import com.gaia.hermes.MessengerHandler;
import com.gaia.hermes.async.jobs.HermesFailureData;
import com.gaia.hermes.bean.ApplicationBean;
import com.gaia.hermes.model.ApplicationModel;
import com.gaia.hermes.statics.Field;
import com.nhb.common.data.PuObject;
import com.nhb.common.db.sql.resp.SqlUpdateResponse;
import com.nhb.common.utils.Converter;

public class CreateApplicationTask extends AbstractTask<PuObject> {
	private MessengerHandler handler;
	private ApplicationBean application;

	public CreateApplicationTask(MessengerHandler handler) {
		this.handler = handler;
	}

	@Override
	public void rollback() {
		// TODO Auto-generated method stub

	}

	@Override
	public void execute(PuObject data) {
		ApplicationModel model = handler.getModelFactory().newModel(ApplicationModel.class);
		String bundleId = data.getString(Field.BUNDLE_ID);
		this.application = model.findByBundleId(bundleId);
		if (this.application == null) {
			ApplicationBean bean = new ApplicationBean();
			bean.autoId();
			bean.setBundleId(bundleId);
			bean.setName(data.getString(Field.NAME));
			bean.setSecretKey(data.getString(Field.SECRET_KEY));
			bean.autoTimestamp();
			bean.setPushNotificationConfigId(
					Converter.uuidToBytes(UUID.fromString(data.getString(Field.PUSH_NOTIFICATION_CONFIG_ID))));
			SqlUpdateResponse response = model.insert(bean);
			if (response.isSuccess()) {
				this.application = bean;
				setSuccessful(true);
			} else {
				setFailureData(new HermesFailureData("error while create application", response.getException()));
			}
		} else {
			setFailureData(new HermesFailureData("bundle " + bundleId + " was exists"));
		}
		completed();
	}

	public ApplicationBean getApplication() {
		return this.application;
	}
}
