package org.jax.mgi.mgd.api.model.mrk.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mrk.entities.EventReason;

@RequestScoped
public class EventReasonDAO extends PostgresSQLDAO<EventReason> {

	protected EventReasonDAO() {
		super(EventReason.class);
	}


}
