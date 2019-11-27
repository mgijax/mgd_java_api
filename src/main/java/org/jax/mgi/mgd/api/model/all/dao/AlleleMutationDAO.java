package org.jax.mgi.mgd.api.model.all.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.all.entities.AlleleMutation;

public class AlleleMutationDAO extends PostgresSQLDAO<AlleleMutation> {
	protected AlleleMutationDAO() {
		super(AlleleMutation.class);
	}
}
