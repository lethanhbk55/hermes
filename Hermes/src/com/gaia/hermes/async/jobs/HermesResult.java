package com.gaia.hermes.async.jobs;

import org.apache.commons.lang.exception.ExceptionUtils;

import com.fullmoon.job.JobResult;
import com.gaia.hermes.statics.Field;
import com.nhb.common.data.PuObject;

public class HermesResult implements JobResult {

	private static final long serialVersionUID = 1L;

	private int status;
	private PuObject data;
	private HermesFailureData error;

	public HermesResult(PuObject data) {
		this.status = 0;
		this.data = data;
	}

	public HermesResult(HermesFailureData error) {
		this.status = 1;
		this.error = error;
	}

	public PuObject toPuObject() {
		PuObject result = new PuObject();
		result.setInteger(Field.STATUS, status);
		if (status == 0) {
			result.set(Field.DATA, data);
		} else {
			if (this.error != null) {
				result.set(Field.MESSAGE, error.getMessage());
				result.set(Field.EX, ExceptionUtils.getFullStackTrace(error.getException()));
			}
		}
		return result;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public PuObject getData() {
		return data;
	}

	public void setData(PuObject data) {
		this.data = data;
	}

	public HermesFailureData getError() {
		return error;
	}

	public void setError(HermesFailureData error) {
		this.error = error;
	}

}
