package org.jax.mgi.mgd.api.model.seq.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.seq.entities.Sequence;

public class SequenceDAO extends PostgresSQLDAO<Sequence> {
	protected SequenceDAO() {
		super(Sequence.class);
	}
}
