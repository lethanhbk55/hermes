package com.gaia.hermes.bean;

import com.nhb.common.db.beans.UUIDBean;

public class ApnsCertificateBean extends UUIDBean {
	private static final long serialVersionUID = 5631292008527010748L;
	private String filePath;
	private long timestamp;
	private String password;

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
