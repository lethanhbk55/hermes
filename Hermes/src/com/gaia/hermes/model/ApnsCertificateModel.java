package com.gaia.hermes.model;

import com.gaia.hermes.bean.ApnsCertificateBean;
import com.gaia.hermes.dao.ApnsCertificateDAO;
import com.nhb.common.db.models.AbstractModel;
import com.nhb.common.db.sql.resp.SqlUpdateResponse;

public class ApnsCertificateModel extends AbstractModel {
	public SqlUpdateResponse insert(ApnsCertificateBean bean) {
		SqlUpdateResponse response = new SqlUpdateResponse();
		if (bean != null) {
			try (ApnsCertificateDAO dao = openDAO(ApnsCertificateDAO.class)) {
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

	public SqlUpdateResponse delete(byte[] id) {
		SqlUpdateResponse response = new SqlUpdateResponse();
		if (id != null) {
			try (ApnsCertificateDAO dao = openDAO(ApnsCertificateDAO.class)) {
				int rowEffected = dao.delete(id);
				response.setSuccess(rowEffected == 1);
				response.setRowEffected(rowEffected);
			} catch (Exception ex) {
				response.setException(ex);
			}
		} else {
			response.setException(new Exception("id tobe deleted cannot be null"));
		}
		return response;
	}

	public ApnsCertificateBean findById(byte[] id) {
		try (ApnsCertificateDAO dao = openDAO(ApnsCertificateDAO.class)) {
			return dao.findById(id);
		}
	}

	public int update(ApnsCertificateBean bean) {
		try (ApnsCertificateDAO dao = openDAO(ApnsCertificateDAO.class)) {
			return dao.update(bean);
		}
	}
}
