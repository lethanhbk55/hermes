package com.gaia.hermes.tasks;

import java.util.UUID;

import com.gaia.hermes.bean.ApnsCertificateBean;
import com.gaia.hermes.model.ApnsCertificateModel;
import com.gaia.hermes.statics.Field;
import com.nhb.common.data.MapTuple;
import com.nhb.common.data.PuObject;
import com.nhb.common.db.sql.resp.SqlUpdateResponse;
import com.nhb.common.transaction.impl.AbstractDistributedTransactionTask;

public class CreateApnsCertificateTask extends AbstractDistributedTransactionTask {
	private ApnsCertificateModel model;
	private ApnsCertificateBean apnsCertificate;

	public CreateApnsCertificateTask(ApnsCertificateModel model) {
		this.model = model;
	}

	@Override
	public void execute(PuObject data) {
		if (data.variableExists(Field.FILE_PATH) && data.variableExists(Field.PASSWORD)) {
			try {
				String filePath = data.getString(Field.FILE_PATH);
				ApnsCertificateBean bean = new ApnsCertificateBean();
				bean.setId(UUID.randomUUID());
				bean.setTimestamp(System.currentTimeMillis() / 1000);
				bean.setFilePath(filePath);
				bean.setPassword(data.getString(Field.PASSWORD));
				SqlUpdateResponse response = this.model.insert(bean);
				if (response.isSuccess()) {
					setSuccessful(true);
					this.setApnsCertificate(bean);
				} else {
					setFailureDetails(PuObject.fromObject(new MapTuple<>(Field.EX,
							"creating apns certificate is failure, error " + response.getException().getMessage())));
					throw new RuntimeException(response.getException());
				}
			} catch (Exception ex) {
				setFailureDetails(PuObject.fromObject(new MapTuple<>(Field.EX, "creating apns certificate is failure")));
				throw new RuntimeException(ex);
			}
		} else {
			setFailureDetails(PuObject.fromObject(new MapTuple<>(Field.EX, "filepath is missing")));
			throw new RuntimeException();
		}
	}

	@Override
	public void rollback() {
		this.model.delete(this.apnsCertificate.getId());
	}

	public ApnsCertificateBean getApnsCertificate() {
		return apnsCertificate;
	}

	private void setApnsCertificate(ApnsCertificateBean apnsCertificate) {
		this.apnsCertificate = apnsCertificate;
	}

}
