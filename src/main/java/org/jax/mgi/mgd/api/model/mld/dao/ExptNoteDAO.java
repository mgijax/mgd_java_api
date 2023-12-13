package org.jax.mgi.mgd.api.model.mld.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mld.entities.ExptNote;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ExptNoteDAO extends PostgresSQLDAO<ExptNote> {
	protected ExptNoteDAO() {
		super(ExptNote.class);
	}
}
