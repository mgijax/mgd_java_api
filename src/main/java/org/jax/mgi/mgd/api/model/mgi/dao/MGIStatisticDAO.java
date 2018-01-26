package org.jax.mgi.mgd.api.model.mgi.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mgi.entities.MGIStatistic;

@RequestScoped
public class MGIStatisticDAO extends PostgresSQLDAO<MGIStatistic> {

	public MGIStatisticDAO() {
		super(MGIStatistic.class);
	}

}
