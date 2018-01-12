package org.jax.mgi.mgd.api.model.mgi.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mgi.entities.PropertyType;

@RequestScoped
public class PropertyTypeDAO extends PostgresSQLDAO<PropertyType> {

	public PropertyTypeDAO() {
		super(PropertyType.class);
	}

}
