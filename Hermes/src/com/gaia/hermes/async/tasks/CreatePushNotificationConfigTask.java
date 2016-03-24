package com.gaia.hermes.async.tasks;

import java.util.UUID;

import com.fullmoon.task.AbstractTask;
import com.gaia.hermes.MessengerHandler;
import com.gaia.hermes.async.jobs.HermesFailureData;
import com.gaia.hermes.bean.PushNotificationConfigBean;
import com.gaia.hermes.model.PushNotificationConfigModel;
import com.gaia.hermes.statics.Field;
import com.nhb.common.data.PuObject;
import com.nhb.common.db.sql.resp.SqlUpdateResponse;
import com.nhb.common.utils.Converter;

public class CreatePushNotificationConfigTask extends AbstractTask<PuObject> {
	private MessengerHandler handler;

	public CreatePushNotificationConfigTask(MessengerHandler handler) {
		this.handler = handler;
	}

	@Override
	public void rollback() {

	}

	@Override
	public void execute(PuObject data) {
		PushNotificationConfigModel model = handler.getModelFactory().newModel(PushNotificationConfigModel.class);
		PushNotificationConfigBean bean = new PushNotificationConfigBean();
		bean.setApnsCertificateId(Converter.uuidToBytes(UUID.fromString(data.getString(Field.APNS_CERTIFICATE_ID))));
		bean.setGcmKeyId(Converter.uuidToBytes(UUID.fromString(data.getString(Field.GCM_KEY_ID))));
		bean.autoId();
		SqlUpdateResponse response = model.insert(bean);
		if (response.isSuccess()) {
			data.setString(Field.PUSH_NOTIFICATION_CONFIG_ID, bean.getUuidString());
			setSuccessful(true);
		} else {
			setFailureData(
					new HermesFailureData("error while create push notfication config", response.getException()));
		}
		completed();
	}

}
