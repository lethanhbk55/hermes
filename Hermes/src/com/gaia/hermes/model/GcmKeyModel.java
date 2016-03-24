package com.gaia.hermes.model;

import com.gaia.hermes.bean.GcmKeyBean;
import com.gaia.hermes.dao.GcmKeyDAO;
import com.nhb.common.db.models.AbstractModel;
import com.nhb.common.db.sql.resp.SqlUpdateResponse;

public class GcmKeyModel extends AbstractModel {
	public SqlUpdateResponse insert(GcmKeyBean bean) {
		SqlUpdateResponse response = new SqlUpdateResponse();
		if (bean != null) {
			try (GcmKeyDAO dao = openDAO(GcmKeyDAO.class)) {
				int rowEffected = dao.insert(bean);
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

	public GcmKeyBean findById(byte[] id) {
		try (GcmKeyDAO dao = openDAO(GcmKeyDAO.class)) {
			return dao.findById(id);
		}
	}

	public SqlUpdateResponse delete(byte[] id) {
		SqlUpdateResponse response = new SqlUpdateResponse();
		if (id != null) {
			try (GcmKeyDAO dao = openDAO(GcmKeyDAO.class)) {
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

	public int update(GcmKeyBean bean) {
		try (GcmKeyDAO dao = openDAO(GcmKeyDAO.class)) {
			return dao.update(bean);
		}
	}

	public GcmKeyBean findByKey(String key) {
		try (GcmKeyDAO dao = openDAO(GcmKeyDAO.class)) {
			return dao.findByKey(key);
		}
	}
}
