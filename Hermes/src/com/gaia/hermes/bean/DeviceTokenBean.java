package com.gaia.hermes.bean;

import com.nhb.common.db.beans.UUIDBean;

public class DeviceTokenBean extends UUIDBean {
	private static final long serialVersionUID = -4582450254152565553L;

	private byte[] deviceId;
	private byte[] applicationId;
	private String notificationToken;
	private long timestamp;

	public byte[] getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(byte[] deviceId) {
		this.deviceId = deviceId;
	}

	public byte[] getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(byte[] applicationId) {
		this.applicationId = applicationId;
	}

	public String getNotificationToken() {
		return notificationToken;
	}

	public void setNotificationToken(String notificationToken) {
		this.notificationToken = notificationToken;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
}
