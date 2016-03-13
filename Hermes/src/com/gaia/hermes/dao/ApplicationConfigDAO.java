package com.gaia.hermes.dao;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.mixins.Transactional;

import com.gaia.hermes.bean.ApplicationConfigBean;
import com.gaia.hermes.mapper.ApplicationConfigMapper;
import com.nhb.common.db.sql.daos.BaseMySqlDAO;

@SuppressWarnings("rawtypes")
@RegisterMapper(ApplicationConfigMapper.class)
public abstract class ApplicationConfigDAO extends BaseMySqlDAO implements Transactional {
	@SqlQuery("select a.id as application_id, a.name as application_name, secret_key, bundle_id, `key` as google_api_key, file_path, ac.password as apple_certificate_password from application as a, push_notification_config as p, apns_certificate as ac, gcm_key as g where a.push_notification_config_id = p.id and p.apns_certificate_id = ac.id and gcm_key_id = g.id and bundle_id = :bundleId and secret_key = :secretKey limit 1")
	public abstract ApplicationConfigBean findByBundleIdAndSecretKey(@Bind("bundleId") String bundleId,
			@Bind("secretKey") String secretKey);

	@SqlQuery("select a.id as application_id, a.name as application_name, secret_key, bundle_id, `key` as google_api_key, file_path, ac.password as apple_certificate_password from application as a, push_notification_config as p, apns_certificate as ac, gcm_key as g where a.push_notification_config_id = p.id and p.apns_certificate_id = ac.id and gcm_key_id = g.id and a.id = :id limit 1")
	public abstract ApplicationConfigBean findById(@Bind("id") byte[] applicationId);
}
