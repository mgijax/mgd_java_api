package org.jax.mgi.mgd.api.model.voc.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.voc.entities.AnnotationType;

@RequestScoped
public class AnnotationTypeDAO extends PostgresSQLDAO<AnnotationType> {

	public AnnotationTypeDAO() {
		super(AnnotationType.class);
	}

}
