package org.jax.mgi.mgd.api.model.mrk.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mrk.entities.MarkerOffset;

@RequestScoped
public class MarkerOffsetDAO extends PostgresSQLDAO<MarkerOffset> {

	protected MarkerOffsetDAO() {
		super(MarkerOffset.class);
	}


}
