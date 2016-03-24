package com.gaia.hermes.async.tasks;

import com.fullmoon.task.AbstractTask;
import com.gaia.hermes.MessengerHandler;
import com.gaia.hermes.async.jobs.HermesFailureData;
import com.gaia.hermes.bean.GcmKeyBean;
import com.gaia.hermes.model.GcmKeyModel;
import com.gaia.hermes.statics.Field;
import com.nhb.common.data.PuObject;
import com.nhb.common.db.sql.resp.SqlUpdateResponse;

public class CreateGCMKeyTask extends AbstractTask<PuObject> {
	private MessengerHandler handler;

	public CreateGCMKeyTask(MessengerHandler handler) {
		this.handler = handler;
	}

	@Override
	public void rollback() {

	}

	@Override
	public void execute(PuObject data) {
		GcmKeyModel model = handler.getModelFactory().newModel(GcmKeyModel.class);
		String key = data.getString(Field.KEY);
		GcmKeyBean bean = model.findByKey(key);
		if (bean != null) {
			data.setString(Field.GCM_KEY_ID, bean.getUuidString());
			setSuccessful(true);
		} else {
			bean = new GcmKeyBean();
			bean.autoId();
			bean.setKey(key);
			bean.autoTimestamp();
			SqlUpdateResponse response = model.insert(bean);
			if (response.isSuccess()) {
				data.setString(Field.GCM_KEY_ID, bean.getUuidString());
				setSuccessful(true);
			} else {
				setFailureData(new HermesFailureData("error while create gcm key", response.getException()));
			}
		}
		completed();
	}
}
