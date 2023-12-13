package org.jax.mgi.mgd.api.model.acc.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.acc.entities.Accession;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AccessionDAO extends PostgresSQLDAO<Accession> {
	protected AccessionDAO() {
		super(Accession.class);
	}
}
