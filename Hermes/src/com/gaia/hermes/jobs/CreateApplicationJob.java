package com.gaia.hermes.jobs;

import com.gaia.hermes.model.ApnsCertificateModel;
import com.gaia.hermes.model.ApplicationModel;
import com.gaia.hermes.model.GcmKeyModel;
import com.gaia.hermes.model.PushNotificationConfigModel;
import com.gaia.hermes.tasks.CreateApnsCertificateTask;
import com.gaia.hermes.tasks.CreateApplicationTask;
import com.gaia.hermes.tasks.CreateGcmKeyTask;
import com.gaia.hermes.tasks.CreatePushNotificationConfigTask;
import com.nhb.common.transaction.impl.AbstractDistributedTransactionJob;

public class CreateApplicationJob extends AbstractDistributedTransactionJob {
	private CreateApnsCertificateTask createApnsCertificateTask;
	private CreateGcmKeyTask createGcmKeyTask;
	private CreatePushNotificationConfigTask createPushNotificationConfigTask;
	private CreateApplicationTask createApplicationTask;

	public CreateApplicationJob(ApplicationModel appModel, ApnsCertificateModel apnsModel, GcmKeyModel gcmModel,
			PushNotificationConfigModel pushModel) {
		createApnsCertificateTask = new CreateApnsCertificateTask(apnsModel);
		createGcmKeyTask = new CreateGcmKeyTask(gcmModel);
		createPushNotificationConfigTask = new CreatePushNotificationConfigTask(pushModel);
		createApplicationTask = new CreateApplicationTask(appModel);

		addTask(createApnsCertificateTask);
		addTask(createGcmKeyTask);
		addTask(createPushNotificationConfigTask);
		addTask(createApplicationTask);
	}

	@Override
	protected void onComplete() {
		setResult(this.createApplicationTask.getApplication().toPuObject());
	}

	public CreateApnsCertificateTask getCreateApnsCertificateTask() {
		return createApnsCertificateTask;
	}

	public CreateGcmKeyTask getCreateGcmKeyTask() {
		return createGcmKeyTask;
	}

	public CreatePushNotificationConfigTask getCreatePushNotificationConfigTask() {
		return createPushNotificationConfigTask;
	}

	public CreateApplicationTask getCreateApplicationTask() {
		return createApplicationTask;
	}
}
