package com.gaia.hermes.dao;

import java.util.Collection;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlBatch;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.BatchChunkSize;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.mixins.Transactional;

import com.gaia.hermes.bean.DeviceBean;
import com.gaia.hermes.mapper.DeviceMapper;
import com.nhb.common.db.sql.daos.BaseMySqlDAO;

@SuppressWarnings("rawtypes")
@RegisterMapper(DeviceMapper.class)
public abstract class DeviceDAO extends BaseMySqlDAO implements Transactional {
	@SqlUpdate("insert into device(id, platform_id, udid, timestamp) values(:id, :platformId, :udid, :timestamp)")
	public abstract int insert(@BindBean DeviceBean device);
	
	@SqlBatch("insert into device(id, platform_id, udid, timestamp) values(:id, :platformId, :udid, :timestamp)")
	@BatchChunkSize(10000)
	public abstract void insert(@BindBean Collection<DeviceBean> devices);
	
	@SqlQuery("select * from device where id = :deviceId limit 1")
	public abstract DeviceBean findById(@Bind("deviceId") byte[] deviceId);
	
	@SqlQuery("select * from device where udid = :udid")
	public abstract DeviceBean findByUdid(@Bind("udid") String udid);
	
	@SqlUpdate("delete from device where id = :id")
	public abstract int delete(@Bind("id") byte[] id);
}
