package com.gaia.hermes.async.jobs;

import com.fullmoon.job.AbstractJob;
import com.gaia.hermes.MessengerHandler;
import com.gaia.hermes.async.tasks.CreateDeviceTask;
import com.gaia.hermes.async.tasks.CreateDeviceTokenTask;
import com.gaia.hermes.async.tasks.FetchApplicationTask;
import com.gaia.hermes.statics.Field;
import com.nhb.common.data.PuObject;

public class RegisterDeviceTokenJob extends AbstractJob<PuObject> {
	private CreateDeviceTask createDeviceTask;
	private FetchApplicationTask fetchApplicationTask;
	private CreateDeviceTokenTask createDeviceTokenTask;

	@SuppressWarnings("unchecked")
	public void buildTasks(MessengerHandler handler) {
		createDeviceTask = new CreateDeviceTask(handler);
		fetchApplicationTask = new FetchApplicationTask(handler);
		createDeviceTokenTask = new CreateDeviceTokenTask(handler);

		addTasks(createDeviceTask, fetchApplicationTask, createDeviceTokenTask);
	}

	@Override
	protected void onCompleted() {
		PuObject data = new PuObject();
		data.setString(Field.DEVICE_TOKEN_ID, createDeviceTokenTask.getDeviceToken().getUuidString());
		setResult(new HermesResult(data));
	}

	@Override
	protected void onIncompleted() {
		setResult(new HermesResult((HermesFailureData) this.getLastDidTask().getFailureData()));
	}
}
