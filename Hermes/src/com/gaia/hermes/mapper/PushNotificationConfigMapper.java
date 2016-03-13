package com.gaia.hermes.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import com.gaia.hermes.bean.PushNoficationConfigBean;
import com.gaia.hermes.statics.DBField;

public class PushNotificationConfigMapper implements ResultSetMapper<PushNoficationConfigBean> {

	@Override
	public PushNoficationConfigBean map(int arg0, ResultSet result, StatementContext arg2) throws SQLException {
		PushNoficationConfigBean bean = new PushNoficationConfigBean();
		bean.setId(result.getBytes(DBField.ID));
		bean.setGcmKeyId(result.getBytes(DBField.GCM_KEY_ID));
		bean.setApnsCertificateId(result.getBytes(DBField.APNS_CERTIFICATE_ID));
		return bean;
	}

}
