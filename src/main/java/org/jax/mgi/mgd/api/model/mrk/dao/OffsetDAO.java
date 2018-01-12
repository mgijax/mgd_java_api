package org.jax.mgi.mgd.api.model.mrk.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mrk.entities.Offset;

@RequestScoped
public class OffsetDAO extends PostgresSQLDAO<Offset> {

	protected OffsetDAO() {
		super(Offset.class);
	}


}
