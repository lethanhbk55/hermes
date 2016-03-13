package com.gaia.hermes.dao;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.mixins.Transactional;

import com.gaia.hermes.bean.GcmKeyBean;
import com.gaia.hermes.mapper.GcmKeyMapper;
import com.nhb.common.db.sql.daos.BaseMySqlDAO;

@SuppressWarnings("rawtypes")
@RegisterMapper(GcmKeyMapper.class)
public abstract class GcmKeyDAO extends BaseMySqlDAO implements Transactional {
	@SqlUpdate("insert into gcm_key(id, `key`, timestamp) values(:id, :key, :timestamp)")
	public abstract int insert(@BindBean GcmKeyBean bean);

	@SqlQuery("select * from gcm_key where id = :id limit 1")
	public abstract GcmKeyBean findById(@Bind("id") byte[] id);

	@SqlUpdate("delete from gcm_key where id = :id")
	public abstract int delete(@Bind("id") byte[] id);

	@SqlUpdate("update gcm_key set `key` = :key where id = :id")
	public abstract int update(@BindBean GcmKeyBean bean);
}
