package org.jax.mgi.mgd.api.model.dag.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.dag.entities.Label;

@RequestScoped
public class LabelDAO extends PostgresSQLDAO<Label> {

	protected LabelDAO() {
		super(Label.class);
	}


}
