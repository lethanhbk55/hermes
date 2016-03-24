package com.gaia.hermes.pushnotification;

public class PushNotificationResult {

	private int success;

	public PushNotificationResult(int successful) {
		this.success = successful;
	}

	public int getSuccess() {
		return success;
	}

	public void setSuccess(int success) {
		this.success = success;
	}
}
