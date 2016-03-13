package com.gaia.hermes.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import com.gaia.hermes.bean.PushNotificationBean;
import com.gaia.hermes.statics.DBField;

public class PushNotificationMapper implements ResultSetMapper<PushNotificationBean> {

	@Override
	public PushNotificationBean map(int arg0, ResultSet result, StatementContext arg2) throws SQLException {
		PushNotificationBean bean = new PushNotificationBean();
		bean.setToken(result.getString(DBField.TOKEN));
		bean.setPlatformId(result.getInt(DBField.PLATFORM_ID));
		return bean;
	}

}
