package org.jax.mgi.mgd.api.model.mld.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mld.entities.MCDataList;

@RequestScoped
public class MCDataListDAO extends PostgresSQLDAO<MCDataList> {

	protected MCDataListDAO() {
		super(MCDataList.class);
	}


}
