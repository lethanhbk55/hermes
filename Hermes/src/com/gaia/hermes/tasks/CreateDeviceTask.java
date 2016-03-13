package com.gaia.hermes.tasks;

import java.util.UUID;

import com.gaia.hermes.bean.DeviceBean;
import com.gaia.hermes.model.DeviceModel;
import com.gaia.hermes.statics.Field;
import com.nhb.common.data.MapTuple;
import com.nhb.common.data.PuObject;
import com.nhb.common.db.sql.resp.SqlUpdateResponse;
import com.nhb.common.transaction.impl.AbstractDistributedTransactionTask;

public class CreateDeviceTask extends AbstractDistributedTransactionTask {
	private DeviceModel model;
	private DeviceBean device;

	public CreateDeviceTask(DeviceModel model) {
		this.model = model;
	}

	@Override
	public void execute(PuObject data) {
		if (data.variableExists(Field.PLATFORM_ID) && data.variableExists(Field.UDID)) {
			try {
				int platformId = data.getInteger(Field.PLATFORM_ID);
				String udid = data.getString(Field.UDID);
				DeviceBean device = this.model.findByUdid(udid);
				if (device == null) {
					DeviceBean bean = new DeviceBean();
					bean.setId(UUID.randomUUID());
					bean.setTimestamp(System.currentTimeMillis() / 1000);
					bean.setPlatformId(platformId);
					bean.setUdid(udid);
					SqlUpdateResponse response = model.insert(bean);
					if (response.isSuccess()) {
						this.device = bean;
						setSuccessful(true);
					} else {
						setFailureDetails(
								PuObject.fromObject(new MapTuple<>(Field.EX, "insert device error " + response.getException().getMessage())));
						throw new RuntimeException(response.getException());
					}
				} else {
					this.device = device;
					setSuccessful(true);
				}
			} catch (Exception ex) {
				setFailureDetails(PuObject.fromObject(new MapTuple<>(Field.EX, "insert device error " + ex.getMessage())));
				throw new RuntimeException(ex);
			}
		} else {
			setFailureDetails(PuObject.fromObject(new MapTuple<>(Field.EX, "parameters are missing")));
			throw new RuntimeException();
		}
	}

	public DeviceBean getDevice() {
		return this.device;
	}

	@Override
	public void rollback() {
		model.delete(device.getId());
	}

}
