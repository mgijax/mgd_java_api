package org.jax.mgi.mgd.api.model.prb.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeNote;

@RequestScoped
public class NotesDAO extends PostgresSQLDAO<ProbeNote> {

	protected NotesDAO() {
		super(ProbeNote.class);
	}


}
