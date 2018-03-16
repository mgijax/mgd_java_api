package org.jax.mgi.mgd.api.model.mrk.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mrk.entities.MarkerType;

public class MarkerTypeDAO extends PostgresSQLDAO<MarkerType> {
	protected MarkerTypeDAO() {
		super(MarkerType.class);
	}
}
