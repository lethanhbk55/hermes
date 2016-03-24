package com.gaia.hermes.async.tasks;

import com.fullmoon.task.AbstractTask;
import com.gaia.hermes.MessengerHandler;
import com.gaia.hermes.async.jobs.HermesFailureData;
import com.gaia.hermes.bean.DeviceBean;
import com.gaia.hermes.model.DeviceModel;
import com.gaia.hermes.statics.Field;
import com.nhb.common.data.PuObject;
import com.nhb.common.db.sql.resp.SqlUpdateResponse;

public class CreateDeviceTask extends AbstractTask<PuObject> {
	private MessengerHandler handler;

	public CreateDeviceTask(MessengerHandler handler) {
		this.handler = handler;
	}

	@Override
	public void rollback() {

	}

	@Override
	public void execute(PuObject data) {
		DeviceModel model = handler.getModelFactory().newModel(DeviceModel.class);
		String udid = data.getString(Field.UDID);
		int platformId = data.getInteger(Field.PLATFORM_ID);
		DeviceBean device = model.findByUdid(udid);
		if (device != null) {
			data.getString(Field.DEVICE_ID, device.getUuidString());
			setSuccessful(true);
		} else {
			DeviceBean bean = new DeviceBean();
			bean.autoId();
			bean.setPlatformId(platformId);
			bean.autoTimestamp();
			SqlUpdateResponse response = model.insert(bean);
			if (response.isSuccess()) {
				data.setString(Field.DEVICE_ID, bean.getUuidString());
				setSuccessful(true);
			} else {
				setFailureData(new HermesFailureData(response.getException()));
			}
		}
		completed();
	}

}
