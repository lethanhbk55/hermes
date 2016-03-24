package com.gaia.hermes.async.jobs;

import com.fullmoon.job.AbstractJob;
import com.gaia.hermes.MessengerHandler;
import com.gaia.hermes.async.tasks.CreateApnsConfigTask;
import com.gaia.hermes.async.tasks.CreateApplicationTask;
import com.gaia.hermes.async.tasks.CreateGCMKeyTask;
import com.gaia.hermes.async.tasks.CreatePushNotificationConfigTask;
import com.nhb.common.data.PuObject;

public class CreateApplicationJob extends AbstractJob<PuObject> {
	private CreateApnsConfigTask createApnsConfigTask;
	private CreateGCMKeyTask createGCMKeyTask;
	private CreatePushNotificationConfigTask createPushNotificationConfigTask;
	private CreateApplicationTask createApplicationTask;

	@SuppressWarnings("unchecked")
	public void buildTasks(MessengerHandler handler) {
		createApnsConfigTask = new CreateApnsConfigTask(handler);
		createGCMKeyTask = new CreateGCMKeyTask(handler);
		createPushNotificationConfigTask = new CreatePushNotificationConfigTask(handler);
		createApplicationTask = new CreateApplicationTask(handler);

		addTasks(createApnsConfigTask, createGCMKeyTask, createPushNotificationConfigTask, createApplicationTask);
	}

	@Override
	protected void onCompleted() {
		setResult(new HermesResult(createApplicationTask.getApplication().toPuObject()));
	}

	@Override
	protected void onIncompleted() {
		HermesFailureData error = (HermesFailureData) this.getLastDidTask().getFailureData();
		setResult(new HermesResult(error));
	}
}
