package org.jax.mgi.mgd.api.model.prb.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeNote;

@RequestScoped
public class ProbeNoteDAO extends PostgresSQLDAO<ProbeNote> {

	protected ProbeNoteDAO() {
		super(ProbeNote.class);
	}


}
