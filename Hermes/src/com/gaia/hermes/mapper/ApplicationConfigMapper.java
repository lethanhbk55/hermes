package com.gaia.hermes.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import com.gaia.hermes.bean.ApplicationConfigBean;
import com.gaia.hermes.statics.DBField;

public class ApplicationConfigMapper implements ResultSetMapper<ApplicationConfigBean> {

	@Override
	public ApplicationConfigBean map(int arg0, ResultSet result, StatementContext arg2) throws SQLException {
		ApplicationConfigBean bean = new ApplicationConfigBean();
		bean.setApplicationName(result.getString(DBField.APPLICATION_NAME));
		bean.setApplicationId(result.getBytes(DBField.APPLICATION_ID));
		bean.setBundleId(result.getString(DBField.BUNDLE_ID));
		bean.setFilePath(result.getString(DBField.FILE_PATH));
		bean.setSecretKey(result.getString(DBField.SECRET_KEY));
		bean.setGoogleApiKey(result.getString(DBField.GOOGLE_API_KEY));
		bean.setAppleCertificatePassword(result.getString(DBField.APPLE_CERTIFICATE_PASSWORD));
		return bean;
	}

}
