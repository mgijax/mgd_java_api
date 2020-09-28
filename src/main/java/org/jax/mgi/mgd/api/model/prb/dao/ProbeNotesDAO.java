package org.jax.mgi.mgd.api.model.prb.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeNotes;

public class ProbeNotesDAO extends PostgresSQLDAO<ProbeNotes> {
	protected ProbeNotesDAO() {
		super(ProbeNotes.class);
	}
}
