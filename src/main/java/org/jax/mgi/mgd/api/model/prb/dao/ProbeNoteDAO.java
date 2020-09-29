package org.jax.mgi.mgd.api.model.prb.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeNote;

public class ProbeNoteDAO extends PostgresSQLDAO<ProbeNote> {
	protected ProbeNoteDAO() {
		super(ProbeNote.class);
	}
}
