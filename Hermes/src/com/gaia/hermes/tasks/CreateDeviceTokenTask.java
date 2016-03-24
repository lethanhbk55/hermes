package com.gaia.hermes.tasks;

import java.util.UUID;

import com.gaia.hermes.bean.ApplicationBean;
import com.gaia.hermes.bean.DeviceBean;
import com.gaia.hermes.bean.DeviceTokenBean;
import com.gaia.hermes.jobs.RegisterDeviceJob;
import com.gaia.hermes.model.DeviceTokenModel;
import com.gaia.hermes.statics.Field;
import com.nhb.common.data.MapTuple;
import com.nhb.common.data.PuObject;
import com.nhb.common.db.sql.resp.SqlUpdateResponse;
import com.nhb.common.transaction.impl.AbstractDistributedTransactionTask;

public class CreateDeviceTokenTask extends AbstractDistributedTransactionTask {
	private DeviceTokenModel model;
	private DeviceTokenBean deviceToken;

	public CreateDeviceTokenTask(DeviceTokenModel model) {
		this.model = model;
	}

	@Override
	public void execute(PuObject data) {
		if (data.variableExists(Field.TOKEN)) {
			try {
				DeviceTokenBean bean = new DeviceTokenBean();
				if (this.getJob() instanceof RegisterDeviceJob) {
					RegisterDeviceJob job = (RegisterDeviceJob) getJob();
					DeviceBean device = job.getCreateDeviceTask().getDevice();
					ApplicationBean application = job.getFetchApplicationTask().getApplication();
					bean.setDeviceId(device.getId());
					bean.setApplicationId(application.getId());
				}

				if (bean.getDeviceId() != null && bean.getApplicationId() != null) {
					DeviceTokenBean deviceToken = this.model.findByDeviceIdAndApplicationId(bean.getDeviceId(),
							bean.getApplicationId());
					if (deviceToken != null) {
						deviceToken.setNotificationToken(data.getString(Field.TOKEN));
						SqlUpdateResponse response = this.model.update(deviceToken);
						if (response.isSuccess()) {
							setSuccessful(true);
							this.deviceToken = deviceToken;
							getLogger().debug("update device token success, token: {}",
									this.deviceToken.getNotificationToken());
						} else {
							setFailureDetails(PuObject.fromObject(new MapTuple<>(Field.EX,
									"update device token is failure, error " + response.getException().getMessage())));
							throw new RuntimeException(response.getException());
						}
					} else {
						bean.setNotificationToken(data.getString(Field.TOKEN));
						bean.setId(UUID.randomUUID());
						bean.setTimestamp(System.currentTimeMillis() / 1000);

						SqlUpdateResponse response = model.insert(bean);
						if (response.isSuccess()) {
							this.deviceToken = bean;
							setSuccessful(true);
						} else {
							setFailureDetails(PuObject
									.fromObject(new MapTuple<>(Field.EX, "creating device token is failure, error "
											+ response.getException().getMessage())));
							throw new RuntimeException(response.getException());
						}
					}
				} else {
					setFailureDetails(PuObject.fromObject(new MapTuple<>(Field.EX,
							"creating device token is failure, deviceId and applicaion must be not null")));
					throw new RuntimeException();
				}
			} catch (Exception ex) {
				setFailureDetails(PuObject.fromObject(new MapTuple<>(Field.EX, "creating device token is failure")));
				getLogger().debug("creating device token is fail, error", ex);
				throw new RuntimeException(ex);
			}
		} else {
			setFailureDetails(
					PuObject.fromObject(new MapTuple<>(Field.EX, "creating device token's paramaters are missing")));
			throw new RuntimeException();
		}
	}

	public DeviceTokenBean getDeviceToken() {
		return this.deviceToken;
	}

	@Override
	public void rollback() {
		model.delete(deviceToken.getId());
	}

}
