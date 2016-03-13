package com.gaia.hermes.dao;

import java.util.Collection;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.mixins.Transactional;

import com.gaia.hermes.bean.DeviceTokenBean;
import com.gaia.hermes.mapper.DeviceTokenMapper;
import com.nhb.common.db.sql.daos.BaseMySqlDAO;

@SuppressWarnings("rawtypes")
@RegisterMapper(DeviceTokenMapper.class)
public abstract class DeviceTokenDAO extends BaseMySqlDAO implements Transactional {
	@SqlUpdate("insert into device_token(id, device_id, application_id, notification_token, timestamp) values(:id, :deviceId, :applicationId, :notificationToken, :timestamp)")
	public abstract int insert(@BindBean DeviceTokenBean bean);

	@SqlUpdate("delete from device_token where id = :id")
	public abstract int delete(@Bind("id") byte[] id);

	@SqlQuery("select * from device_token as t, device as d, platform as p where d.id = t.device_id and d.platform_id = p.id and p.id = :platformId")
	public abstract Collection<DeviceTokenBean> findByPlatformId(@Bind("platformId") int platformId);

	@SqlQuery("select * from device_token")
	public abstract Collection<DeviceTokenBean> findAll();

	@SqlQuery("select * from device_token where device_id = :deviceId and application_id = :applicationId limit 1")
	public abstract DeviceTokenBean findByDeviceIdAndApplicationId(@Bind("deviceId") byte[] deviceId,
			@Bind("applicationId") byte[] applicationId);

	@SqlUpdate("update device_token set notification_token = :notificationToken where id = :id")
	public abstract int update(@BindBean DeviceTokenBean deviceToken);

	@SqlQuery("select * from device_token where notification_token = :notificationToken limit 1")
	public abstract DeviceTokenBean findByToken(@Bind("notificationToken") String token);
}
