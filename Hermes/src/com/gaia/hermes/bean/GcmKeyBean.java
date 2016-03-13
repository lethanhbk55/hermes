package com.gaia.hermes.bean;

import com.nhb.common.db.beans.UUIDBean;

public class GcmKeyBean extends UUIDBean {
	private static final long serialVersionUID = 2147882766295192992L;

	private String key;
	private long timestamp;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
}
