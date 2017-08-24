package org.jax.mgi.mgd.api.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.entities.Vocabulary;

@RequestScoped
public class VocabularyDAO extends PostgresSQLDAO<Vocabulary> {

	public VocabularyDAO() {
		myClass = Vocabulary.class;
	}

}
