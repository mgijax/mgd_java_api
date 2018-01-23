package org.jax.mgi.mgd.api.model.crs.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.crs.entities.CrossReference;

@RequestScoped
public class CrossReferenceDAO extends PostgresSQLDAO<CrossReference> {

	public CrossReferenceDAO() {
		super(CrossReference.class);
	}

}
