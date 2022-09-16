package org.jax.mgi.mgd.api.model.voc.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.voc.entities.VocabularyDAG;

public class VocabularyDAGDAO extends PostgresSQLDAO<VocabularyDAG> {
	protected VocabularyDAGDAO() {
		super(VocabularyDAG.class);
	}
}
