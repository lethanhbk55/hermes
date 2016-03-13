package com.gaia.hermes.bean;

import com.nhb.common.db.beans.AbstractBean;

public class ApplicationConfigBean extends AbstractBean {
	private static final long serialVersionUID = 6215332119500917196L;

	private String filePath;
	private String appleCertificatePassword;
	private String googleApiKey;
	private String secretKey;
	private String bundleId;
	private byte[] applicationId;
	private String applicationName;

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getGoogleApiKey() {
		return googleApiKey;
	}

	public void setGoogleApiKey(String googleApiKey) {
		this.googleApiKey = googleApiKey;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getBundleId() {
		return bundleId;
	}

	public void setBundleId(String bundleId) {
		this.bundleId = bundleId;
	}

	public byte[] getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(byte[] applicationId) {
		this.applicationId = applicationId;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public String getAppleCertificatePassword() {
		return appleCertificatePassword;
	}

	public void setAppleCertificatePassword(String appleCertificatePassword) {
		this.appleCertificatePassword = appleCertificatePassword;
	}
}
