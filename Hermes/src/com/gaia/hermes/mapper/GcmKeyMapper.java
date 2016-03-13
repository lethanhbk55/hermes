package com.gaia.hermes.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import com.gaia.hermes.bean.GcmKeyBean;
import com.gaia.hermes.statics.DBField;

public class GcmKeyMapper implements ResultSetMapper<GcmKeyBean> {

	@Override
	public GcmKeyBean map(int arg0, ResultSet result, StatementContext arg2) throws SQLException {
		GcmKeyBean bean = new GcmKeyBean();
		bean.setId(result.getBytes(DBField.ID));
		bean.setKey(result.getString(DBField.KEY));
		bean.setTimestamp(result.getLong(DBField.TIMESTAMP));
		return bean;
	}

}
