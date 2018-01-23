package org.jax.mgi.mgd.api.model.mld.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mld.entities.Notes;

@RequestScoped
public class NotesDAO extends PostgresSQLDAO<Notes> {

	protected NotesDAO() {
		super(Notes.class);
	}


}
