package org.jax.mgi.mgd.api.model.mld.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mld.entities.MLDNote;

public class MLDNoteDAO extends PostgresSQLDAO<MLDNote> {
	protected MLDNoteDAO() {
		super(MLDNote.class);
	}
}
