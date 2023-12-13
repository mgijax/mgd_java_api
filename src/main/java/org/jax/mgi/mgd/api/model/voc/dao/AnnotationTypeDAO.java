package org.jax.mgi.mgd.api.model.voc.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.voc.entities.AnnotationType;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AnnotationTypeDAO extends PostgresSQLDAO<AnnotationType> {
	public AnnotationTypeDAO() {
		super(AnnotationType.class);
	}
}
