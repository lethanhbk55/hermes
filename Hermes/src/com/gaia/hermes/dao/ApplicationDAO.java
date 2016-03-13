package com.gaia.hermes.dao;

import java.util.Collection;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.mixins.Transactional;

import com.gaia.hermes.bean.ApplicationBean;
import com.gaia.hermes.mapper.ApplicationMapper;
import com.nhb.common.db.sql.daos.BaseMySqlDAO;

@SuppressWarnings("rawtypes")
@RegisterMapper(ApplicationMapper.class)
public abstract class ApplicationDAO extends BaseMySqlDAO implements Transactional {
	@SqlUpdate("insert into application(id, name, bundle_id, secret_key, push_notification_config_id, timestamp) values(:id, :name, :bundleId, :secretKey, :pushNotificationConfigId, :timestamp)")
	public abstract int insert(@BindBean ApplicationBean application);

	@SqlQuery("select * from application where id = :id limit 1")
	public abstract ApplicationBean findById(@Bind("id") byte[] id);

	@SqlQuery("select * from application where bundle_id = :bundleId limit 1")
	public abstract ApplicationBean findByBundleId(@Bind("bundleId") String bundleId);

	@SqlQuery("select * from application where bundle_id = :bundleId and secret_key = :secretKey limit 1")
	public abstract ApplicationBean findByBundleIdAndSecretKey(@Bind("bundleId") String bundleId,
			@Bind("secretKey") String secretKey);

	@SqlUpdate("delete from application  where id = :id")
	public abstract int delete(@Bind("id") byte[] id);

	@SqlQuery("select * from application order by timestamp desc")
	public abstract Collection<ApplicationBean> findAll();
}
