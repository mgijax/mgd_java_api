package org.jax.mgi.mgd.api.model.voc.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.voc.entities.Annotation;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AnnotationDAO extends PostgresSQLDAO<Annotation> {
	public AnnotationDAO() {
		super(Annotation.class);
	}
}
