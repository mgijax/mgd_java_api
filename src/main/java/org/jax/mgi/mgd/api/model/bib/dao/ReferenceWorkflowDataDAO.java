package org.jax.mgi.mgd.api.model.bib.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.bib.entities.ReferenceWorkflowData;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ReferenceWorkflowDataDAO extends PostgresSQLDAO<ReferenceWorkflowData> {
	protected ReferenceWorkflowDataDAO() {
		super(ReferenceWorkflowData.class);
	}
}
