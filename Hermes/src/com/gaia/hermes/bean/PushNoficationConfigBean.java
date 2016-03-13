package com.gaia.hermes.bean;

import com.nhb.common.db.beans.UUIDBean;

public class PushNoficationConfigBean extends UUIDBean {
	private static final long serialVersionUID = 4759162404639115569L;

	private byte[] apnsCertificateId;
	private byte[] gcmKeyId;

	public byte[] getApnsCertificateId() {
		return apnsCertificateId;
	}

	public void setApnsCertificateId(byte[] apnsCertificateId) {
		this.apnsCertificateId = apnsCertificateId;
	}

	public byte[] getGcmKeyId() {
		return gcmKeyId;
	}

	public void setGcmKeyId(byte[] gcmKeyId) {
		this.gcmKeyId = gcmKeyId;
	}
}
