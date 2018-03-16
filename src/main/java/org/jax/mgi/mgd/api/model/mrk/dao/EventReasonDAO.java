package org.jax.mgi.mgd.api.model.mrk.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mrk.entities.EventReason;

public class EventReasonDAO extends PostgresSQLDAO<EventReason> {
	protected EventReasonDAO() {
		super(EventReason.class);
	}
}
