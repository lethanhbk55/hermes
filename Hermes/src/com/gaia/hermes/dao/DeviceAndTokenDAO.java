package com.gaia.hermes.dao;

import java.util.List;

import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import com.gaia.hermes.bean.DeviceAndTokenBean;
import com.gaia.hermes.mapper.DeviceAndTokenMapper;
import com.nhb.common.db.sql.daos.BaseMySqlDAO;

@RegisterMapper(DeviceAndTokenMapper.class)
public abstract class DeviceAndTokenDAO extends BaseMySqlDAO {

	@SqlQuery("select notification_token, platform_id from device_token, device where device_token.device_id = device.id")
	public abstract List<DeviceAndTokenBean> findAll();
}
