package com.gaia.hermes.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import com.gaia.hermes.bean.DeviceTokenBean;
import com.gaia.hermes.statics.DBField;

public class DeviceTokenMapper implements ResultSetMapper<DeviceTokenBean> {

	@Override
	public DeviceTokenBean map(int arg0, ResultSet result, StatementContext arg2) throws SQLException {
		DeviceTokenBean bean = new DeviceTokenBean();
		bean.setId(result.getBytes(DBField.ID));
		bean.setApplicationId(result.getBytes(DBField.APPLICATION_ID));
		bean.setDeviceId(result.getBytes(DBField.DEVICE_ID));
		bean.setNotificationToken(result.getString(DBField.NOTIFICATION_TOKEN));
		return bean;
	}

}
