package com.gaia.hermes.model;

import com.gaia.hermes.bean.ApplicationConfigBean;
import com.gaia.hermes.dao.ApplicationConfigDAO;
import com.nhb.common.db.models.AbstractModel;

public class ApplicationConfigModel extends AbstractModel {
	public ApplicationConfigBean findByBundleIdAndSecretKey(String bundleId, String secretKey) {
		try (ApplicationConfigDAO dao = openDAO(ApplicationConfigDAO.class)) {
			return dao.findByBundleIdAndSecretKey(bundleId, secretKey);
		}
	}

	public ApplicationConfigBean findById(byte[] applicationId) {
		try (ApplicationConfigDAO dao = openDAO(ApplicationConfigDAO.class)) {
			return dao.findById(applicationId);
		}
	}
}
