package org.jax.mgi.mgd.api.model.voc.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.voc.entities.EvidenceProperty;

@RequestScoped
public class EvidencePropertyDAO extends PostgresSQLDAO<EvidenceProperty> {

	public EvidencePropertyDAO() {
		super(EvidenceProperty.class);
	}

}
