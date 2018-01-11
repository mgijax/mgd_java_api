package org.jax.mgi.mgd.api.model.mgi.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mgi.entities.TranslationType;

@RequestScoped
public class TranslationTypeDAO extends PostgresSQLDAO<TranslationType> {

	public TranslationTypeDAO() {
		super(TranslationType.class);
	}

}
