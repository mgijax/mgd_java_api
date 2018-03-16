package org.jax.mgi.mgd.api.model.gxd.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.gxd.entities.Specimen;

public class SpecimenDAO extends PostgresSQLDAO<Specimen> {
	protected SpecimenDAO() {
		super(Specimen.class);
	}
}
