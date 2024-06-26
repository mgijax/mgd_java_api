package org.jax.mgi.mgd.api.model.gxd.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.gxd.entities.HTSample;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class HTSampleDAO extends PostgresSQLDAO<HTSample> {
	protected HTSampleDAO() {
		super(HTSample.class);
	}
}
