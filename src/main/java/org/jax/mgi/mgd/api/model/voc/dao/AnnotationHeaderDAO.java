package org.jax.mgi.mgd.api.model.voc.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.voc.entities.AnnotHeader;

@RequestScoped
public class AnnotHeaderDAO extends PostgresSQLDAO<AnnotHeader> {

	public AnnotHeaderDAO() {
		super(AnnotHeader.class);
	}

}
