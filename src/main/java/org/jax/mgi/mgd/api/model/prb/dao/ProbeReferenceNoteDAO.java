package org.jax.mgi.mgd.api.model.prb.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeReferenceNote;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProbeReferenceNoteDAO extends PostgresSQLDAO<ProbeReferenceNote> {
	protected ProbeReferenceNoteDAO() {
		super(ProbeReferenceNote.class);
	}
}
