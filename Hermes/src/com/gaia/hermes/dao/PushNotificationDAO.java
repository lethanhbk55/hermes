package com.gaia.hermes.dao;

import java.util.Collection;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.mixins.Transactional;

import com.gaia.hermes.bean.PushNotificationBean;
import com.gaia.hermes.mapper.PushNotificationMapper;
import com.nhb.common.db.sql.daos.BaseMySqlDAO;

@SuppressWarnings("rawtypes")
@RegisterMapper(PushNotificationMapper.class)
public abstract class PushNotificationDAO extends BaseMySqlDAO implements Transactional {
	@SqlQuery("select d.platform_id, d.notification_token as token, file_path, `key` as google_api_key from device_token as t, device as d, platform as p, application as a, push_notification_config as c, gcm_key as g, apns_certificate as ac where t.device_id = d.id and d.platform_id = p.id and t.applicaiton_id = a.id and a.push_notification_config_id = c.id and c.apns_certificate_id = ac.id and g.id = c.gcm_key_id")
	public abstract Collection<PushNotificationBean> findAll();

	@SqlQuery("select d.platform_id, t.notification_token as token from device as d, device_token as t, platform as p where d.id = t.device_id and d.platform_id = p.id and application_id = :applicationId")
	public abstract Collection<PushNotificationBean> findByApplicationId(@Bind("applicationId") byte[] applicationId);
	
	@SqlQuery("select d.platform_id, t.notification_token as token from device as d, device_token as t, platform as p where d.id = t.device_id and d.platform_id = p.id and application_id = :applicationId and p.id = :platformId")
	public abstract Collection<PushNotificationBean> findByApplicationIdAndPlatformId(@Bind("applicationId") byte[] applicationId, @Bind("platformId") int platformId);
}
