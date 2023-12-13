package org.jax.mgi.mgd.api.model.prb.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeNote;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProbeNoteDAO extends PostgresSQLDAO<ProbeNote> {
	protected ProbeNoteDAO() {
		super(ProbeNote.class);
	}
}
