package org.jax.mgi.mgd.api.model.mrk.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mrk.entities.Event;

public class EventDAO extends PostgresSQLDAO<Event> {
	protected EventDAO() {
		super(Event.class);
	}
}
