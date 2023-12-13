package org.jax.mgi.mgd.api.model.all.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.all.entities.AlleleCellLine;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AlleleCellLineDAO extends PostgresSQLDAO<AlleleCellLine> {
	protected AlleleCellLineDAO() {
		super(AlleleCellLine.class);
	}
}
