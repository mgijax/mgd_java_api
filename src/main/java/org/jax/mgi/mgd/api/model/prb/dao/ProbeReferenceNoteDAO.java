package org.jax.mgi.mgd.api.model.prb.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeReferenceNote;

@RequestScoped
public class ProbeReferenceNoteDAO extends PostgresSQLDAO<ProbeReferenceNote> {

	protected ProbeReferenceNoteDAO() {
		super(ProbeReferenceNote.class);
	}


}
