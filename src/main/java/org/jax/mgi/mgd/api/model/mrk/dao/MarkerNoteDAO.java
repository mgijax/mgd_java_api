package org.jax.mgi.mgd.api.model.mrk.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mrk.entities.MarkerNote;

public class MarkerNoteDAO extends PostgresSQLDAO<MarkerNote> {
	protected MarkerNoteDAO() {
		super(MarkerNote.class);
	}
}
