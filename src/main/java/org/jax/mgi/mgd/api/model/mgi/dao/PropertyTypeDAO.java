package org.jax.mgi.mgd.api.model.mgi.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mgi.entities.PropertyType;

public class PropertyTypeDAO extends PostgresSQLDAO<PropertyType> {
	public PropertyTypeDAO() {
		super(PropertyType.class);
	}
}
