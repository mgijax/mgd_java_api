package org.jax.mgi.mgd.api.model.all.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.all.entities.AlleleCellLineDerivation;

@RequestScoped
public class AlleleCellLineDerivationDAO extends PostgresSQLDAO<AlleleCellLineDerivation> {

	protected AlleleCellLineDerivationDAO() {
		super(AlleleCellLineDerivation.class);
	}


}
