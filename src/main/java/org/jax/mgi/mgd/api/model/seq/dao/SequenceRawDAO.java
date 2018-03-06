package org.jax.mgi.mgd.api.model.seq.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.seq.entities.SequenceRaw;

public class SequenceRawDAO extends PostgresSQLDAO<SequenceRaw> {
	protected SequenceRawDAO() {
		super(SequenceRaw.class);
	}
}
