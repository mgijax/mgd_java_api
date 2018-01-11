package org.jax.mgi.mgd.api.model.all.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.all.entities.AlleleCellline;

@RequestScoped
public class AlleleCelllineDAO extends PostgresSQLDAO<AlleleCellline> {

	protected AlleleCelllineDAO() {
		super(AlleleCellline.class);
	}


}
