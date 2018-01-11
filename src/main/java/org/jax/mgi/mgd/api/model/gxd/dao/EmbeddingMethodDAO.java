package org.jax.mgi.mgd.api.model.gxd.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.gxd.entities.EmbeddingMethod;

@RequestScoped
public class EmbeddingMethodDAO extends PostgresSQLDAO<EmbeddingMethod> {

	protected EmbeddingMethodDAO() {
		super(EmbeddingMethod.class);
	}


}
