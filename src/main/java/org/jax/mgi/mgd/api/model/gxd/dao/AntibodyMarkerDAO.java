package org.jax.mgi.mgd.api.model.gxd.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.gxd.entities.AntibodyMarker;

public class AntibodyMarkerDAO extends PostgresSQLDAO<AntibodyMarker> {
	protected AntibodyMarkerDAO() {
		super(AntibodyMarker.class);
	}
}
