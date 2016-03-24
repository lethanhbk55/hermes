package com.gaia.hermes.async.tasks;

import java.util.UUID;

import com.fullmoon.task.AbstractTask;
import com.gaia.hermes.MessengerHandler;
import com.gaia.hermes.async.jobs.HermesFailureData;
import com.gaia.hermes.bean.DeviceTokenBean;
import com.gaia.hermes.model.DeviceTokenModel;
import com.gaia.hermes.statics.Field;
import com.nhb.common.data.PuObject;
import com.nhb.common.db.sql.resp.SqlUpdateResponse;
import com.nhb.common.utils.Converter;

public class CreateDeviceTokenTask extends AbstractTask<PuObject> {
	private MessengerHandler handler;
	private DeviceTokenBean deviceToken;

	public CreateDeviceTokenTask(MessengerHandler handler) {
		this.handler = handler;
	}

	@Override
	public void rollback() {

	}

	@Override
	public void execute(PuObject data) {
		DeviceTokenModel model = handler.getModelFactory().newModel(DeviceTokenModel.class);
		String deviceIdString = data.getString(Field.DEVICE_ID);
		byte[] deviceId = Converter.uuidToBytes(UUID.fromString(deviceIdString));
		byte[] applicationId = Converter.uuidToBytes(UUID.fromString(data.getString(Field.APPLICATION_ID)));
		String token = data.getString(Field.TOKEN);
		this.deviceToken = model.findByDeviceIdAndApplicationId(deviceId, applicationId);
		if (deviceToken != null) {
			deviceToken.setNotificationToken(token);
			SqlUpdateResponse resp = model.update(deviceToken);
			if (resp.isSuccess()) {
				setSuccessful(true);
			} else {
				setFailureData(new HermesFailureData("update device token for device " + deviceIdString + " get error",
						resp.getException()));
			}
		} else {
			deviceToken = new DeviceTokenBean();
			deviceToken.autoId();
			deviceToken.setApplicationId(applicationId);
			deviceToken.setDeviceId(deviceId);
			deviceToken.autoTimestamp();

			SqlUpdateResponse resp = model.insert(deviceToken);
			if (resp.isSuccess()) {
				setSuccessful(true);
			} else {
				setFailureData(new HermesFailureData("error while create device token for device " + deviceIdString,
						resp.getException()));
			}
		}
		completed();
	}

	public DeviceTokenBean getDeviceToken() {
		return this.deviceToken;
	}
}
