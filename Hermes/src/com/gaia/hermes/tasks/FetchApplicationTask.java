package com.gaia.hermes.tasks;

import com.gaia.hermes.bean.ApplicationBean;
import com.gaia.hermes.model.ApplicationModel;
import com.gaia.hermes.statics.Field;
import com.nhb.common.data.MapTuple;
import com.nhb.common.data.PuObject;
import com.nhb.common.transaction.impl.AbstractDistributedTransactionTask;

public class FetchApplicationTask extends AbstractDistributedTransactionTask {

	private ApplicationModel model;
	private ApplicationBean application;

	public FetchApplicationTask(ApplicationModel model) {
		this.model = model;
	}

	@Override
	public void execute(PuObject data) {
		if (data.variableExists(Field.BUNDLE_ID)) {
			try {
				String bundleId = data.getString(Field.BUNDLE_ID);
				ApplicationBean bean = model.findByBundleId(bundleId);
				if (bean != null) {
					this.application = bean;
					setSuccessful(true);
				} else {
					setFailureDetails(PuObject.fromObject(new MapTuple<>(Field.EX, "bundleId isn't found")));
					throw new RuntimeException();
				}
			} catch (Exception ex) {
				setFailureDetails(PuObject.fromObject(new MapTuple<>(Field.EX, "bundleId is not found")));
				getLogger().debug("find application error", ex);
				throw new RuntimeException(ex);
			}
		} else {
			setFailureDetails(PuObject.fromObject(new MapTuple<>(Field.EX, "paramters are missing")));
			throw new RuntimeException();
		}
	}

	public ApplicationBean getApplication() {
		return this.application;
	}

	@Override
	public void rollback() {

	}

}
