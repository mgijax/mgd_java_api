package org.jax.mgi.mgd.api.model.gxd.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.gxd.entities.HTRawSample;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class HTRawSampleDAO extends PostgresSQLDAO<HTRawSample> {
	protected HTRawSampleDAO() {
		super(HTRawSample.class);
	}
}
