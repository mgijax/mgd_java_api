package org.jax.mgi.mgd.api.model.gxd.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.gxd.entities.Visualization;

@RequestScoped
public class VisualizationDAO extends PostgresSQLDAO<Visualization> {

	protected VisualizationDAO() {
		super(Visualization.class);
	}


}
