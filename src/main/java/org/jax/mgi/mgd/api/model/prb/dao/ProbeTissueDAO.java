package org.jax.mgi.mgd.api.model.prb.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeTissue;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProbeTissueDAO extends PostgresSQLDAO<ProbeTissue> {
	protected ProbeTissueDAO() {
		super(ProbeTissue.class);
	}
}
