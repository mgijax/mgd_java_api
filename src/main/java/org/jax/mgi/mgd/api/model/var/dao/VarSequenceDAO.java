package org.jax.mgi.mgd.api.model.var.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.var.entities.VarSequence;

public class VarSequenceDAO extends PostgresSQLDAO<VarSequence> {
	protected VarSequenceDAO() {
		super(VarSequence.class);
	}
}