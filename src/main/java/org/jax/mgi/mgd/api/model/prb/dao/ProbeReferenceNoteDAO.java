package org.jax.mgi.mgd.api.model.prb.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.prb.entities.RefNotes;

@RequestScoped
public class RefNotesDAO extends PostgresSQLDAO<RefNotes> {

	protected RefNotesDAO() {
		super(RefNotes.class);
	}


}
