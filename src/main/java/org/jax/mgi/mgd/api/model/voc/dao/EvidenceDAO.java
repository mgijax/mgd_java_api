package org.jax.mgi.mgd.api.model.voc.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.voc.entities.Evidence;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EvidenceDAO extends PostgresSQLDAO<Evidence> {
	protected EvidenceDAO() {
		super(Evidence.class);
	}
}
