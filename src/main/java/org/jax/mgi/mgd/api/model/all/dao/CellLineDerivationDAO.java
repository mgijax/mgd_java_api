package org.jax.mgi.mgd.api.model.all.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.all.entities.CellLineDerivation;

@RequestScoped
public class CellLineDerivationDAO extends PostgresSQLDAO<CellLineDerivation> {

	protected CellLineDerivationDAO() {
		super(CellLineDerivation.class);
	}


}
