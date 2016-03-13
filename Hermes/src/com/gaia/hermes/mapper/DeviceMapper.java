package com.gaia.hermes.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import com.gaia.hermes.bean.DeviceBean;
import com.gaia.hermes.statics.DBField;

public class DeviceMapper implements ResultSetMapper<DeviceBean> {

	@Override
	public DeviceBean map(int arg0, ResultSet result, StatementContext arg2) throws SQLException {
		DeviceBean device = new DeviceBean();
		device.setId(result.getBytes(DBField.ID));
		device.setPlatformId(result.getInt(DBField.PLATFORM_ID));
		device.setTimestamp(result.getLong(DBField.TIMESTAMP));
		device.setUdid(result.getString(DBField.UDID));
		return device;
	}

}
