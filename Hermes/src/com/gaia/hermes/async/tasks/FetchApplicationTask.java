package com.gaia.hermes.async.tasks;

import com.fullmoon.task.AbstractTask;
import com.gaia.hermes.MessengerHandler;
import com.gaia.hermes.async.jobs.HermesFailureData;
import com.gaia.hermes.bean.ApplicationBean;
import com.gaia.hermes.model.ApplicationModel;
import com.gaia.hermes.statics.Field;
import com.nhb.common.data.PuObject;

public class FetchApplicationTask extends AbstractTask<PuObject> {
	private MessengerHandler handler;

	public FetchApplicationTask(MessengerHandler handler) {
		this.handler = handler;
	}

	@Override
	public void rollback() {

	}

	@Override
	public void execute(PuObject data) {
		ApplicationModel model = handler.getModelFactory().newModel(ApplicationModel.class);
		String bundleId = data.getString(Field.BUNDLE_ID);
		ApplicationBean application = model.findByBundleId(bundleId);
		if (application != null) {
			data.setString(Field.APPLICATION_ID, application.getUuidString());
			setSuccessful(true);
		} else {
			setFailureData(new HermesFailureData("application with bundle " + bundleId + " was not found"));
		}
		completed();
	}

}
