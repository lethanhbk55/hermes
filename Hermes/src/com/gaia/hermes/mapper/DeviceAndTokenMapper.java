package com.gaia.hermes.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import com.gaia.hermes.bean.DeviceAndTokenBean;
import com.gaia.hermes.statics.DBField;

public class DeviceAndTokenMapper implements ResultSetMapper<DeviceAndTokenBean> {

	@Override
	public DeviceAndTokenBean map(int arg0, ResultSet result, StatementContext arg2) throws SQLException {
		DeviceAndTokenBean bean = new DeviceAndTokenBean();
		bean.setToken(result.getString(DBField.NOTIFICATION_TOKEN));
		bean.setPlatformId(result.getInt(DBField.PLATFORM_ID));
		return bean;
	}

}
