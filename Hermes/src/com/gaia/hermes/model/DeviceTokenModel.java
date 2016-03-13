package com.gaia.hermes.model;

import java.util.Collection;

import com.gaia.hermes.bean.DeviceTokenBean;
import com.gaia.hermes.dao.DeviceTokenDAO;
import com.nhb.common.db.models.AbstractModel;
import com.nhb.common.db.sql.resp.SqlUpdateResponse;

public class DeviceTokenModel extends AbstractModel {
	public SqlUpdateResponse insert(DeviceTokenBean bean) {
		SqlUpdateResponse response = new SqlUpdateResponse();
		if (bean != null) {
			try (DeviceTokenDAO dao = openDAO(DeviceTokenDAO.class)) {
				int rowEffected = dao.insert(bean);
				response.setRowEffected(rowEffected);
				response.setSuccess(rowEffected == 1);
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
			try (DeviceTokenDAO dao = openDAO(DeviceTokenDAO.class)) {
				int rowEffected = dao.delete(id);
				response.setRowEffected(rowEffected);
				response.setSuccess(rowEffected == 1);
			} catch (Exception ex) {
				response.setException(ex);
			}
		} else {
			response.setException(new Exception("id tobe deleted cannot be null"));
		}
		return response;
	}

	public Collection<DeviceTokenBean> findByPlatformId(int platformId) {
		try (DeviceTokenDAO dao = openDAO(DeviceTokenDAO.class)) {
			return dao.findByPlatformId(platformId);
		}
	}

	public Collection<DeviceTokenBean> findAll() {
		try (DeviceTokenDAO dao = openDAO(DeviceTokenDAO.class)) {
			return dao.findAll();
		}
	}

	public DeviceTokenBean findByDeviceIdAndApplicationId(byte[] deviceId, byte[] applicationId) {
		try (DeviceTokenDAO dao = openDAO(DeviceTokenDAO.class)) {
			return dao.findByDeviceIdAndApplicationId(deviceId, applicationId);
		}
	}

	public SqlUpdateResponse update(DeviceTokenBean bean) {
		SqlUpdateResponse response = new SqlUpdateResponse();
		if (bean != null) {
			try (DeviceTokenDAO dao = openDAO(DeviceTokenDAO.class)) {
				int rowEffected = dao.update(bean);
				response.setRowEffected(rowEffected);
				response.setSuccess(rowEffected == 1);
			} catch (Exception ex) {
				response.setException(ex);
			}
		} else {
			response.setException(new Exception("bean tobe updated cannot be null"));
		}
		return response;
	}

	public DeviceTokenBean findByToken(String token) {
		try (DeviceTokenDAO dao = openDAO(DeviceTokenDAO.class)) {
			return dao.findByToken(token);
		}
	}
}
