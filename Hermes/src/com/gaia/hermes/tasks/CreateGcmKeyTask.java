package com.gaia.hermes.tasks;

import java.util.UUID;

import com.gaia.hermes.bean.GcmKeyBean;
import com.gaia.hermes.model.GcmKeyModel;
import com.gaia.hermes.statics.Field;
import com.nhb.common.data.MapTuple;
import com.nhb.common.data.PuObject;
import com.nhb.common.db.sql.resp.SqlUpdateResponse;
import com.nhb.common.transaction.impl.AbstractDistributedTransactionTask;

public class CreateGcmKeyTask extends AbstractDistributedTransactionTask {
	private GcmKeyModel model;
	private GcmKeyBean gcmKey;

	public CreateGcmKeyTask(GcmKeyModel model) {
		this.model = model;
	}

	@Override
	public void execute(PuObject data) {
		if (data.variableExists(Field.KEY)) {
			try {
				GcmKeyBean bean = new GcmKeyBean();
				bean.setId(UUID.randomUUID());
				bean.setKey(data.getString(Field.KEY));
				bean.setTimestamp(System.currentTimeMillis() / 1000);
				SqlUpdateResponse response = this.model.insert(bean);
				if (response.isSuccess()) {
					setSuccessful(true);
					this.setGcmKey(bean);
				} else {
					setFailureDetails(PuObject.fromObject(new MapTuple<>(Field.EX,
							"creating gcm key is fail, error " + response.getException().getMessage())));
					throw new RuntimeException(response.getException());
				}
			} catch (Exception ex) {
				setFailureDetails(PuObject.fromObject(new MapTuple<>(Field.EX, "creating gcm key is fail")));
				getLogger().error("creating gcm key error, ", ex);
				throw new RuntimeException(ex);
			}
		} else {
			setFailureDetails(PuObject.fromObject(new MapTuple<>(Field.EX, "key parameter is missing")));
			throw new RuntimeException();
		}
	}

	@Override
	public void rollback() {
		model.delete(gcmKey.getId());
	}

	public GcmKeyBean getGcmKey() {
		return gcmKey;
	}

	private void setGcmKey(GcmKeyBean gcmKey) {
		this.gcmKey = gcmKey;
	}

}
