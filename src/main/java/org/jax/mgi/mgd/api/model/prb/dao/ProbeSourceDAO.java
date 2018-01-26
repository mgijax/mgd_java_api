package org.jax.mgi.mgd.api.model.prb.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.prb.entities.Source;

@RequestScoped
public class SourceDAO extends PostgresSQLDAO<Source> {

	protected SourceDAO() {
		super(Source.class);
	}


}
