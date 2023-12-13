package org.jax.mgi.mgd.api.model.voc.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.voc.entities.AnnotationHeader;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AnnotationHeaderDAO extends PostgresSQLDAO<AnnotationHeader> {
	public AnnotationHeaderDAO() {
		super(AnnotationHeader.class);
	}
}
