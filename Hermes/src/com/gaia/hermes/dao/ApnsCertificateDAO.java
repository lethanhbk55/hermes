package com.gaia.hermes.dao;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.mixins.Transactional;

import com.gaia.hermes.bean.ApnsCertificateBean;
import com.gaia.hermes.mapper.ApnsCertificateMapper;
import com.nhb.common.db.sql.daos.BaseMySqlDAO;

@SuppressWarnings("rawtypes")
@RegisterMapper(ApnsCertificateMapper.class)
public abstract class ApnsCertificateDAO extends BaseMySqlDAO implements Transactional {
	@SqlUpdate("insert into apns_certificate(id, file_path, password, timestamp) values(:id, :filePath, :password, :timestamp)")
	public abstract int insert(@BindBean ApnsCertificateBean bean);

	@SqlUpdate("delete from apns_certificate where id = :id")
	public abstract int delete(@Bind("id") byte[] id);

	@SqlQuery("select * from apns_certificate where id = :id limit 1")
	public abstract ApnsCertificateBean findById(@Bind("id") byte[] id);

	@SqlUpdate("update apns_certificate set file_path = :filePath, password = :password where id = :id")
	public abstract int update(@BindBean ApnsCertificateBean bean);
}
