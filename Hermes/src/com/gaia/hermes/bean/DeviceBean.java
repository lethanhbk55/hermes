package com.gaia.hermes.bean;

import com.nhb.common.db.beans.UUIDBean;

public class DeviceBean extends UUIDBean {
	private static final long serialVersionUID = 1L;
	private int platformId;
	private String udid;
	private long timestamp;

	public int getPlatformId() {
		return platformId;
	}

	public void setPlatformId(int platformId) {
		this.platformId = platformId;
	}

	public String getUdid() {
		return udid;
	}

	public void setUdid(String udid) {
		this.udid = udid;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public void autoTimestamp() {
		this.timestamp = (int) (System.currentTimeMillis() / 1000);
	}
}
