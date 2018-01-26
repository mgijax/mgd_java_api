package org.jax.mgi.mgd.api.model.voc.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.voc.entities.Annotation;

@RequestScoped
public class AnnotationDAO extends PostgresSQLDAO<Annotation> {

	public AnnotationDAO() {
		super(Annotation.class);
	}

}
