package com.gaia.hermes.dao;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.mixins.Transactional;

import com.gaia.hermes.bean.PushNoficationConfigBean;
import com.gaia.hermes.mapper.PushNotificationConfigMapper;
import com.nhb.common.db.sql.daos.BaseMySqlDAO;

@SuppressWarnings("rawtypes")
@RegisterMapper(PushNotificationConfigMapper.class)
public abstract class PushNotificationConfigDAO extends BaseMySqlDAO implements Transactional {
	@SqlUpdate("insert into push_notification_config(id, apns_certificate_id, gcm_key_id) values(:id, :apnsCertificateId, :gcmKeyId)")
	public abstract int insert(@BindBean PushNoficationConfigBean bean);

	@SqlUpdate("delete from push_notification_config where id = :id")
	public abstract int delete(@Bind("id") byte[] id);

	@SqlQuery("select * from push_notification_config where id = :id")
	public abstract PushNoficationConfigBean findById(@Bind("id") byte[] id);
}
