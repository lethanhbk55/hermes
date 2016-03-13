package com.gaia.hermes.model;

import com.gaia.hermes.bean.PlatformBean;
import com.gaia.hermes.dao.PlatformDAO;
import com.nhb.common.db.models.AbstractModel;
import com.nhb.common.db.sql.resp.SqlUpdateResponse;

public class PlatformModel extends AbstractModel {
	public SqlUpdateResponse insert(PlatformBean bean) {
		SqlUpdateResponse response = new SqlUpdateResponse();
		if (bean != null) {
			try (PlatformDAO dao = openDAO(PlatformDAO.class)) {
				int rowEffected = dao.insert(bean);
				response.setRowEffected(rowEffected);
				response.setSuccess(rowEffected == 1);
			} catch (Exception ex) {
				response.setException(ex);
			}
		} else {
			response.setException(new Exception("bean tobe inserted cannot be null"));
		}
		return response;
	}
}
