package org.jax.mgi.mgd.api.model.voc.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.voc.entities.AnnotType;

@RequestScoped
public class AnnotTypeDAO extends PostgresSQLDAO<AnnotType> {

	public AnnotTypeDAO() {
		super(AnnotType.class);
	}

}
