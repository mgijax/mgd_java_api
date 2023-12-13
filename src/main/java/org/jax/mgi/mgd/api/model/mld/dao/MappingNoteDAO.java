package org.jax.mgi.mgd.api.model.mld.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mld.entities.MappingNote;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MappingNoteDAO extends PostgresSQLDAO<MappingNote> {
	protected MappingNoteDAO() {
		super(MappingNote.class);
	}
}
