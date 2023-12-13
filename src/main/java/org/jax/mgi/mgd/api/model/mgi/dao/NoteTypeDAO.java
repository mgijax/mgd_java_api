package org.jax.mgi.mgd.api.model.mgi.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mgi.entities.NoteType;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class NoteTypeDAO extends PostgresSQLDAO<NoteType> {
	public NoteTypeDAO() {
		super(NoteType.class);
	}
}
