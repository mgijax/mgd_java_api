package org.jax.mgi.mgd.api.model.voc.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.voc.entities.Evidence;

@RequestScoped
public class EvidenceDAO extends PostgresSQLDAO<Evidence> {

	protected EvidenceDAO() {
		super(Evidence.class);
	}

}
