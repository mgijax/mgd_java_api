package org.jax.mgi.mgd.api.model.voc.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.voc.entities.Evidence_Property;

@RequestScoped
public class Evidence_PropertyDAO extends PostgresSQLDAO<Evidence_Property> {

	public Evidence_PropertyDAO() {
		super(Evidence_Property.class);
	}

}
