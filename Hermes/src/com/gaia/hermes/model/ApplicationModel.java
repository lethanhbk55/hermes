package com.gaia.hermes.model;

import java.util.Collection;

import com.gaia.hermes.bean.ApplicationBean;
import com.gaia.hermes.dao.ApplicationDAO;
import com.nhb.common.db.models.AbstractModel;
import com.nhb.common.db.sql.resp.SqlUpdateResponse;

public class ApplicationModel extends AbstractModel {
	public SqlUpdateResponse insert(ApplicationBean application) {
		SqlUpdateResponse response = new SqlUpdateResponse();
		if (application != null) {
			try (ApplicationDAO dao = openDAO(ApplicationDAO.class)) {
				int rowEffected = dao.insert(application);
				response.setSuccess(rowEffected == 1);
				response.setRowEffected(rowEffected);
			} catch (Exception ex) {
				response.setException(ex);
			}
		} else {
			response.setException(new Exception("bean tobe inserted cannot be null"));
		}
		return response;
	}

	public ApplicationBean findById(byte[] id) {
		try (ApplicationDAO dao = openDAO(ApplicationDAO.class)) {
			return dao.findById(id);
		}
	}

	public ApplicationBean findByBundleId(String bundleId) {
		try (ApplicationDAO dao = openDAO(ApplicationDAO.class)) {
			return dao.findByBundleId(bundleId);
		}
	}

	public ApplicationBean findByBundleIdAndSecretKey(String bundleId, String secretKey) {
		try (ApplicationDAO dao = openDAO(ApplicationDAO.class)) {
			return dao.findByBundleIdAndSecretKey(bundleId, secretKey);
		}
	}

	public SqlUpdateResponse delete(byte[] id) {
		SqlUpdateResponse response = new SqlUpdateResponse();
		if (id != null) {
			try (ApplicationDAO dao = openDAO(ApplicationDAO.class)) {
				int rowEffected = dao.delete(id);
				response.setSuccess(rowEffected == 1);
				response.setRowEffected(rowEffected);
			} catch (Exception ex) {
				response.setException(ex);
			}
		} else {
			response.setException(new Exception("id cannot be null"));
		}
		return response;
	}

	public Collection<ApplicationBean> findAll() {
		try (ApplicationDAO dao = openDAO(ApplicationDAO.class)) {
			return dao.findAll();
		}
	}
}
