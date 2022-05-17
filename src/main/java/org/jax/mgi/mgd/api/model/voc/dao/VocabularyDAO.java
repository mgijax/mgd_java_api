package org.jax.mgi.mgd.api.model.voc.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.voc.entities.Vocabulary;

public class VocabularyDAO extends PostgresSQLDAO<Vocabulary> {
	protected VocabularyDAO() {
		super(Vocabulary.class);
	}
}
