package org.jax.mgi.mgd.api.model.bib.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.bib.entities.LTReferenceWorkflowData;

public class LTReferenceWorkflowDataDAO extends PostgresSQLDAO<LTReferenceWorkflowData> {
	protected LTReferenceWorkflowDataDAO() {
		super(LTReferenceWorkflowData.class);
	}
}
