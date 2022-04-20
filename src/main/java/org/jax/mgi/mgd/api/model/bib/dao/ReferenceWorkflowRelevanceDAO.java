package org.jax.mgi.mgd.api.model.bib.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.bib.entities.ReferenceWorkflowRelevance;

public class ReferenceWorkflowRelevanceDAO extends PostgresSQLDAO<ReferenceWorkflowRelevance> {
	protected ReferenceWorkflowRelevanceDAO() {
		super(ReferenceWorkflowRelevance.class);
	}
}
