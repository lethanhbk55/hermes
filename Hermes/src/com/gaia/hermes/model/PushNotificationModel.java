package com.gaia.hermes.model;

import java.util.Collection;

import com.gaia.hermes.bean.PushNotificationBean;
import com.gaia.hermes.dao.PushNotificationDAO;
import com.nhb.common.db.models.AbstractModel;

public class PushNotificationModel extends AbstractModel {
	public Collection<PushNotificationBean> findAll() {
		try (PushNotificationDAO dao = openDAO(PushNotificationDAO.class)) {
			return dao.findAll();
		}
	}

	public Collection<PushNotificationBean> findByApplicationId(byte[] applicationId) {
		try (PushNotificationDAO dao = openDAO(PushNotificationDAO.class)) {
			return dao.findByApplicationId(applicationId);
		}
	}

	public Collection<PushNotificationBean> findByApplicationIdAndPlatformId(byte[] applicationId, int platformId) {
		try (PushNotificationDAO dao = openDAO(PushNotificationDAO.class)) {
			return dao.findByApplicationIdAndPlatformId(applicationId, platformId);
		}
	}
}
