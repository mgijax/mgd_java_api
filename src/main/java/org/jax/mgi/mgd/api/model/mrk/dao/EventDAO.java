package org.jax.mgi.mgd.api.model.mrk.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mrk.entities.Event;

@RequestScoped
public class EventDAO extends PostgresSQLDAO<Event> {

	protected EventDAO() {
		super(Event.class);
	}


}
