package org.jax.mgi.mgd.api.service;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.jax.mgi.mgd.api.dao.PostgresSQLDAO;
import org.jax.mgi.mgd.api.dao.VocabularyDAO;
import org.jax.mgi.mgd.api.entities.Vocabulary;

@RequestScoped
public class VocabService extends ServiceInterface<Vocabulary> {

	@Inject
	private VocabularyDAO vocabularyDAO;
	
	@Override
	public PostgresSQLDAO<Vocabulary> getDAO() {
		return vocabularyDAO;
	}

}
