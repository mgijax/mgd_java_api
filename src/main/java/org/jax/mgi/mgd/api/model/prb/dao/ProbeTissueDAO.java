package org.jax.mgi.mgd.api.model.prb.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeTissue;

@RequestScoped
public class ProbeTissueDAO extends PostgresSQLDAO<ProbeTissue> {

	protected ProbeTissueDAO() {
		super(ProbeTissue.class);
	}


}
