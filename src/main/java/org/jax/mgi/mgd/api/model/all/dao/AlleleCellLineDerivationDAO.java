package org.jax.mgi.mgd.api.model.all.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.all.entities.AlleleCellLineDerivation;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AlleleCellLineDerivationDAO extends PostgresSQLDAO<AlleleCellLineDerivation> {
	protected AlleleCellLineDerivationDAO() {
		super(AlleleCellLineDerivation.class);
	}
}
