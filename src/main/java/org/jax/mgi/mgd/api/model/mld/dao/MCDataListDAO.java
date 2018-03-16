package org.jax.mgi.mgd.api.model.mld.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mld.entities.MCDataList;

public class MCDataListDAO extends PostgresSQLDAO<MCDataList> {
	protected MCDataListDAO() {
		super(MCDataList.class);
	}
}
