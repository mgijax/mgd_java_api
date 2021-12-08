package org.jax.mgi.mgd.api.model.bib.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.bib.entities.ReferenceWorkflowTag;

public class ReferenceWorkflowTagDAO extends PostgresSQLDAO<ReferenceWorkflowTag> {
	protected ReferenceWorkflowTagDAO() {
		super(ReferenceWorkflowTag.class);
	}
}
