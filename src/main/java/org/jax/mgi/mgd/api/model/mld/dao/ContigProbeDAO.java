package org.jax.mgi.mgd.api.model.mld.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mld.entities.ContigProbe;

@RequestScoped
public class ContigProbeDAO extends PostgresSQLDAO<ContigProbe> {

	protected ContigProbeDAO() {
		super(ContigProbe.class);
	}


}
