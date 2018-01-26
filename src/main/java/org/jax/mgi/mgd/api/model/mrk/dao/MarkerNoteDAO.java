package org.jax.mgi.mgd.api.model.mrk.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mrk.entities.MarkerNote;

@RequestScoped
public class MarkerNoteDAO extends PostgresSQLDAO<MarkerNote> {

	protected MarkerNoteDAO() {
		super(MarkerNote.class);
	}


}
