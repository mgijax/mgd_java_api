package org.jax.mgi.mgd.api.model.bib.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.bib.entities.ReferenceNote;

public class ReferenceNoteDAO extends PostgresSQLDAO<ReferenceNote> {
	protected ReferenceNoteDAO() {
		super(ReferenceNote.class);
	}
}
