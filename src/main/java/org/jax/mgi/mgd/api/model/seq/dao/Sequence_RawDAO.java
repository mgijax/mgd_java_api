package org.jax.mgi.mgd.api.model.seq.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.seq.entities.Sequence_Raw;

@RequestScoped
public class Sequence_RawDAO extends PostgresSQLDAO<Sequence_Raw> {

	protected Sequence_RawDAO() {
		super(Sequence_Raw.class);
	}


}
