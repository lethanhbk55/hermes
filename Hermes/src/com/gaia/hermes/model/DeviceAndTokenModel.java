package com.gaia.hermes.model;

import java.util.List;

import com.gaia.hermes.bean.DeviceAndTokenBean;
import com.gaia.hermes.dao.DeviceAndTokenDAO;
import com.nhb.common.db.models.AbstractModel;

public class DeviceAndTokenModel extends AbstractModel {

	public List<DeviceAndTokenBean> findAll() {
		try (DeviceAndTokenDAO dao = openDAO(DeviceAndTokenDAO.class)) {
			return dao.findAll();
		}
	}
}
