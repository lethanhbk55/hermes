package com.gaia.hermes.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import com.gaia.hermes.bean.ApplicationBean;
import com.gaia.hermes.statics.DBField;
import com.gaia.hermes.statics.Field;

public class ApplicationMapper implements ResultSetMapper<ApplicationBean> {

	@Override
	public ApplicationBean map(int arg0, ResultSet result, StatementContext arg2) throws SQLException {
		ApplicationBean app = new ApplicationBean();
		app.setName(result.getString(Field.NAME));
		app.setBundleId(result.getString(DBField.BUNDLE_ID));
		app.setId(result.getBytes(DBField.ID));
		app.setPushNotificationConfigId(result.getBytes(DBField.PUSH_NOTIFICATION_CONFIG_ID));
		app.setSecretKey(result.getString(DBField.SECRET_KEY));
		app.setTimestamp(result.getLong(DBField.TIMESTAMP));
		return app;
	}

}
