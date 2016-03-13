package com.gaia.hermes.dao;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.mixins.Transactional;

import com.gaia.hermes.bean.PlatformBean;
import com.gaia.hermes.mapper.PlatformMapper;
import com.nhb.common.db.sql.daos.BaseMySqlDAO;

@SuppressWarnings("rawtypes")
@RegisterMapper(PlatformMapper.class)
public abstract class PlatformDAO extends BaseMySqlDAO implements Transactional{
	@SqlUpdate("insert into platform(name, display_name) values(:name, :displayName)")
	public abstract int insert(@BindBean PlatformBean platform);
	
	@SqlQuery("select * from platform where name = :name limit 1")
	public abstract PlatformDAO findByName(@Bind("name") String name);
}
