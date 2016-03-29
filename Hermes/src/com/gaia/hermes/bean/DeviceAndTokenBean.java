package com.gaia.hermes.bean;

import com.nhb.common.db.beans.AbstractBean;

public class DeviceAndTokenBean extends AbstractBean {
	private static final long serialVersionUID = 8080002718934471659L;

	private String token;
	private int platformId;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public int getPlatformId() {
		return platformId;
	}

	public void setPlatformId(int platformId) {
		this.platformId = platformId;
	}

}
