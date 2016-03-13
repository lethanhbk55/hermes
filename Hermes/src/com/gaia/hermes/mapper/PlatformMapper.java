package com.gaia.hermes.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import com.gaia.hermes.bean.PlatformBean;
import com.gaia.hermes.statics.DBField;

public class PlatformMapper implements ResultSetMapper<PlatformBean> {

	@Override
	public PlatformBean map(int arg0, ResultSet result, StatementContext arg2) throws SQLException {
		PlatformBean bean = new PlatformBean();
		bean.setDisplayName(result.getString(DBField.DISPLAY_NAME));
		bean.setId(result.getInt(DBField.ID));
		bean.setName(result.getString(DBField.NAME));
		return bean;
	}

}
