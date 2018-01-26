package org.jax.mgi.mgd.api.model.voc.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.voc.entities.AnnotationHeader;

@RequestScoped
public class AnnotationHeaderDAO extends PostgresSQLDAO<AnnotationHeader> {

	public AnnotationHeaderDAO() {
		super(AnnotationHeader.class);
	}

}
