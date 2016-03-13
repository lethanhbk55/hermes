package com.gaia.hermes.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import com.gaia.hermes.bean.ApnsCertificateBean;
import com.gaia.hermes.statics.DBField;
import com.gaia.hermes.statics.Field;

public class ApnsCertificateMapper implements ResultSetMapper<ApnsCertificateBean> {

	@Override
	public ApnsCertificateBean map(int arg0, ResultSet result, StatementContext arg2) throws SQLException {
		ApnsCertificateBean bean = new ApnsCertificateBean();
		bean.setFilePath(result.getString(DBField.FILE_PATH));
		bean.setId(result.getBytes(DBField.ID));
		bean.setTimestamp(result.getLong(DBField.TIMESTAMP));
		bean.setPassword(result.getString(Field.PASSWORD));
		return bean;
	}

}
