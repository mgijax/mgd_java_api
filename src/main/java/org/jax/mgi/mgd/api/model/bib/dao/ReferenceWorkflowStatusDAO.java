package org.jax.mgi.mgd.api.model.bib.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.bib.entities.ReferenceWorkflowStatus;

public class ReferenceWorkflowStatusDAO extends PostgresSQLDAO<ReferenceWorkflowStatus> {
	protected ReferenceWorkflowStatusDAO() {
		super(ReferenceWorkflowStatus.class);
	}
}
