package com.gaia.hermes.jobs;

import com.gaia.hermes.model.ApplicationModel;
import com.gaia.hermes.model.DeviceModel;
import com.gaia.hermes.model.DeviceTokenModel;
import com.gaia.hermes.statics.Field;
import com.gaia.hermes.tasks.CreateDeviceTask;
import com.gaia.hermes.tasks.CreateDeviceTokenTask;
import com.gaia.hermes.tasks.FetchApplicationTask;
import com.nhb.common.data.MapTuple;
import com.nhb.common.data.PuObject;
import com.nhb.common.transaction.impl.AbstractDistributedTransactionJob;

public class RegisterDeviceJob extends AbstractDistributedTransactionJob {
	private CreateDeviceTask createDeviceTask;
	private CreateDeviceTokenTask createDeviceTokenTask;
	private FetchApplicationTask fetchApplicationTask;

	public RegisterDeviceJob(DeviceModel deviceModel, DeviceTokenModel deviceTokenModel,
			ApplicationModel applicationModel) {
		createDeviceTask = new CreateDeviceTask(deviceModel);
		createDeviceTokenTask = new CreateDeviceTokenTask(deviceTokenModel);
		fetchApplicationTask = new FetchApplicationTask(applicationModel);
		this.addTask(createDeviceTask);
		this.addTask(fetchApplicationTask);
		this.addTask(createDeviceTokenTask);
	}

	@Override
	protected void onComplete() {
		setResult(PuObject.fromObject(new MapTuple<>(Field.DEVICE_TOKEN_ID, createDeviceTokenTask.getDeviceToken().getUuidString())));
	}

	public CreateDeviceTask getCreateDeviceTask() {
		return createDeviceTask;
	}

	public CreateDeviceTokenTask getCreateDeviceTokenTask() {
		return createDeviceTokenTask;
	}

	public FetchApplicationTask getFetchApplicationTask() {
		return fetchApplicationTask;
	}
}
