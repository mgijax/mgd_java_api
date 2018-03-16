package org.jax.mgi.mgd.api.model.prb.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeReferenceNote;

public class ProbeReferenceNoteDAO extends PostgresSQLDAO<ProbeReferenceNote> {
	protected ProbeReferenceNoteDAO() {
		super(ProbeReferenceNote.class);
	}
}
