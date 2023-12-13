package org.jax.mgi.mgd.api.model.mrk.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mrk.entities.MarkerNote;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MarkerNoteDAO extends PostgresSQLDAO<MarkerNote> {
	protected MarkerNoteDAO() {
		super(MarkerNote.class);
	}
}
