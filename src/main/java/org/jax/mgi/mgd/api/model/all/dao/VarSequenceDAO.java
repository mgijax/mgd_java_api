package org.jax.mgi.mgd.api.model.all.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.all.entities.VarSequence;

public class VarSequenceDAO extends PostgresSQLDAO<VarSequence> {
	protected VarSequenceDAO() {
		super(VarSequence.class);
	}
}
