package org.jax.mgi.mgd.api.model.mld.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mld.entities.Contig;

public class ContigDAO extends PostgresSQLDAO<Contig> {
	protected ContigDAO() {
		super(Contig.class);
	}
}
