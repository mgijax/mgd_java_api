package org.jax.mgi.mgd.api.model.seq.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.seq.entities.Sequence;

@RequestScoped
public class SequenceDAO extends PostgresSQLDAO<Sequence> {

	protected SequenceDAO() {
		super(Sequence.class);
	}


}
