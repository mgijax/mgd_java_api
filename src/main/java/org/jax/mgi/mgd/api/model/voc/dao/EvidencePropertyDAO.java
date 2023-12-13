package org.jax.mgi.mgd.api.model.voc.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.voc.entities.EvidenceProperty;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EvidencePropertyDAO extends PostgresSQLDAO<EvidenceProperty> {
	public EvidencePropertyDAO() {
		super(EvidenceProperty.class);
	}
}
