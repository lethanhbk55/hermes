package com.gaia.hermes.bean;

import com.gaia.hermes.statics.Field;
import com.nhb.common.data.PuObject;
import com.nhb.common.db.beans.UUIDBean;
import com.nhb.common.utils.Converter;

public class ApplicationBean extends UUIDBean {
	private static final long serialVersionUID = -2417734766737304092L;

	private String name;
	private String bundleId;
	private String secretKey;
	private byte[] pushNotificationConfigId;
	private long timestamp;

	public String getBundleId() {
		return bundleId;
	}

	public void setBundleId(String bundleId) {
		this.bundleId = bundleId;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public byte[] getPushNotificationConfigId() {
		return pushNotificationConfigId;
	}

	public void setPushNotificationConfigId(byte[] pushNotificationConfigId) {
		this.pushNotificationConfigId = pushNotificationConfigId;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public PuObject toPuObject() {
		PuObject puo = new PuObject();
		puo.setString(Field.APPLICATION_NAME, this.getName());
		puo.setString(Field.APPLICATION_ID, this.getUuidString());
		puo.setString(Field.BUNDLE_ID, this.getBundleId());
		puo.setString(Field.SECRET_KEY, this.getSecretKey());
		puo.setString(Field.PUSH_NOTIFICATION_CONFIG_ID,
				Converter.bytesToUUID(this.getPushNotificationConfigId()).toString());
		puo.setLong(Field.TIMESTAMP, this.getTimestamp());
		return puo;
	}

	public void autoTimestamp() {
		this.timestamp = (int) (System.currentTimeMillis() / 1000);
	}
}
