package org.jax.mgi.mgd.api.model.mld.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mld.entities.Concordance;

@RequestScoped
public class ConcordanceDAO extends PostgresSQLDAO<Concordance> {

	protected ConcordanceDAO() {
		super(Concordance.class);
	}


}
