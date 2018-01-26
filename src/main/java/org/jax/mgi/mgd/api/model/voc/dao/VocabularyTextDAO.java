package org.jax.mgi.mgd.api.model.voc.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.voc.entities.VocabularyText;

@RequestScoped
public class VocabularyTextDAO extends PostgresSQLDAO<VocabularyText> {

	public VocabularyTextDAO() {
		super(VocabularyText.class);
	}

}
