package com.gaia.hermes.async.tasks;

import com.fullmoon.task.AbstractTask;
import com.gaia.hermes.MessengerHandler;
import com.gaia.hermes.async.jobs.HermesFailureData;
import com.gaia.hermes.bean.ApnsCertificateBean;
import com.gaia.hermes.model.ApnsCertificateModel;
import com.gaia.hermes.statics.Field;
import com.nhb.common.data.PuObject;
import com.nhb.common.db.sql.resp.SqlUpdateResponse;

public class CreateApnsConfigTask extends AbstractTask<PuObject> {
	private MessengerHandler handler;

	public CreateApnsConfigTask(MessengerHandler handler) {
		this.handler = handler;
	}

	@Override
	public void rollback() {

	}

	@Override
	public void execute(PuObject data) {
		ApnsCertificateModel model = handler.getModelFactory().newModel(ApnsCertificateModel.class);
		ApnsCertificateBean bean = new ApnsCertificateBean();
		bean.setFilePath(data.getString(Field.FILE_PATH));
		bean.setPassword(data.getString(Field.PASSWORD, ""));
		bean.autoId();
		bean.autoTimestamp();
		SqlUpdateResponse response = model.insert(bean);
		if (response.isSuccess()) {
			data.setString(Field.APNS_CERTIFICATE_ID, bean.getUuidString());
			setSuccessful(true);
		} else {
			setFailureData(new HermesFailureData("error while create apns certificate", response.getException()));
		}
		completed();
	}

}
