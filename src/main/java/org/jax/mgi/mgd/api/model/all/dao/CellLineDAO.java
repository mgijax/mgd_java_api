package org.jax.mgi.mgd.api.model.all.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.all.entities.CellLine;

public class CellLineDAO extends PostgresSQLDAO<CellLine> {
	protected CellLineDAO() {
		super(CellLine.class);
	}
}
