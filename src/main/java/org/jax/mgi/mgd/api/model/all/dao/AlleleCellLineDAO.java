package org.jax.mgi.mgd.api.model.all.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.all.entities.CellLine;

@RequestScoped
public class CellLineDAO extends PostgresSQLDAO<CellLine> {

	protected CellLineDAO() {
		super(CellLine.class);
	}


}
