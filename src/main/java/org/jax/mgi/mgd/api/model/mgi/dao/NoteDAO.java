package org.jax.mgi.mgd.api.model.mgi.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mgi.entities.Note;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class NoteDAO extends PostgresSQLDAO<Note> {
	public NoteDAO() {
		super(Note.class);
	}
}
