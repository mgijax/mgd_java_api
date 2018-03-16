package org.jax.mgi.mgd.api.model.gxd.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.gxd.entities.VisualizationMethod;

public class VisualizationMethodDAO extends PostgresSQLDAO<VisualizationMethod> {
	protected VisualizationMethodDAO() {
		super(VisualizationMethod.class);
	}
}
