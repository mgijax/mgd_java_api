package org.jax.mgi.mgd.api.model.mrk.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mrk.entities.MarkerNote;

@RequestScoped
public class NotesDAO extends PostgresSQLDAO<MarkerNote> {

	protected NotesDAO() {
		super(MarkerNote.class);
	}


}
