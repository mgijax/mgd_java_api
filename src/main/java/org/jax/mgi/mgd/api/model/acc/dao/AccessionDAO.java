package org.jax.mgi.mgd.api.model.acc.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.acc.entities.Accession;

@RequestScoped
public class AccessionDAO extends PostgresSQLDAO<Accession> {

	protected AccessionDAO() {
		super(Accession.class);
	}

}
