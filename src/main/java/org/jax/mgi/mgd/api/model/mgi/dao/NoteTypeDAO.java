package org.jax.mgi.mgd.api.model.mgi.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mgi.entities.NoteType;

@RequestScoped
public class NoteTypeDAO extends PostgresSQLDAO<NoteType> {

	public NoteTypeDAO() {
		super(NoteType.class);
	}

}
