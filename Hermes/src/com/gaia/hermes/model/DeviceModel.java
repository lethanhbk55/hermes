package com.gaia.hermes.model;

import com.gaia.hermes.bean.DeviceBean;
import com.gaia.hermes.dao.DeviceDAO;
import com.nhb.common.db.models.AbstractModel;
import com.nhb.common.db.sql.resp.SqlUpdateResponse;

public class DeviceModel extends AbstractModel {

	public SqlUpdateResponse insert(DeviceBean device) {
		SqlUpdateResponse response = new SqlUpdateResponse();
		if (device != null) {
			try (DeviceDAO dao = openDAO(DeviceDAO.class)) {
				int rowEffected = dao.insert(device);
				response.setSuccess(rowEffected == 1);
				response.setRowEffected(rowEffected);
			} catch (Exception ex) {
				response.setException(ex);
			}
		} else {
			response.setException(new Exception("Bean tobe inserted cannot be null"));
		}
		return response;
	}

	public SqlUpdateResponse delete(byte[] id) {
		SqlUpdateResponse response = new SqlUpdateResponse();
		if (id != null) {
			try (DeviceDAO dao = openDAO(DeviceDAO.class)) {
				int rowEffected = dao.delete(id);
				response.setSuccess(rowEffected == 1);
				response.setRowEffected(rowEffected);
			} catch (Exception ex) {
				getLogger().error("insert user error: ", ex);
				response.setException(ex);
			}
		} else {
			response.setException(new Exception("id tobe inserted cannot be null"));
		}
		return response;
	}

	public DeviceBean findByUdid(String udid) {
		try (DeviceDAO dao = openDAO(DeviceDAO.class)) {
			return dao.findByUdid(udid);
		}
	}

	public DeviceBean findById(byte[] deviceId) {
		try (DeviceDAO dao = openDAO(DeviceDAO.class)) {
			return dao.findById(deviceId);
		}
	}
}
