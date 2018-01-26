package org.jax.mgi.mgd.api.model.mrk.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mrk.entities.MarkerType;

@RequestScoped
public class MarkerTypeDAO extends PostgresSQLDAO<MarkerType> {

	protected MarkerTypeDAO() {
		super(MarkerType.class);
	}


}
