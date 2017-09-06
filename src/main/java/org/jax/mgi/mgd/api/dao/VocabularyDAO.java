package org.jax.mgi.mgd.api.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.entities.Vocabulary;

@RequestScoped
public class VocabularyDAO extends PostgresSQLDAO<Vocabulary> {

	protected VocabularyDAO() {
		super(Vocabulary.class);
	}

}
