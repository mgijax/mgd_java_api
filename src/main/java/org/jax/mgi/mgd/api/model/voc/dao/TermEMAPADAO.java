package org.jax.mgi.mgd.api.model.voc.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.voc.entities.TermEMAPA;

@RequestScoped
public class TermEMAPADAO extends PostgresSQLDAO<TermEMAPA> {

	public TermEMAPADAO() {
		super(TermEMAPA.class);
	}

}
