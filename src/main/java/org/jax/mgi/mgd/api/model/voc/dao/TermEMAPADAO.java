package org.jax.mgi.mgd.api.model.voc.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.voc.entities.TermEMAPA;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TermEMAPADAO extends PostgresSQLDAO<TermEMAPA> {
	public TermEMAPADAO() {
		super(TermEMAPA.class);
	}
}
