package com.gaia.hermes.model;

import com.gaia.hermes.bean.PushNoficationConfigBean;
import com.gaia.hermes.dao.PushNotificationConfigDAO;
import com.nhb.common.db.models.AbstractModel;
import com.nhb.common.db.sql.resp.SqlUpdateResponse;

public class PushNotificationConfigModel extends AbstractModel {
	public SqlUpdateResponse insert(PushNoficationConfigBean bean) {
		SqlUpdateResponse response = new SqlUpdateResponse();
		if (bean != null) {
			try (PushNotificationConfigDAO dao = openDAO(PushNotificationConfigDAO.class)) {
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
			try (PushNotificationConfigDAO dao = openDAO(PushNotificationConfigDAO.class)) {
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

	public PushNoficationConfigBean findById(byte[] id) {
		try (PushNotificationConfigDAO dao = openDAO(PushNotificationConfigDAO.class)) {
			return dao.findById(id);
		}
	}
}
