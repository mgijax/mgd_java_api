package org.jax.mgi.mgd.api.model.mld.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mld.entities.Expt_Notes;

@RequestScoped
public class Expt_NotesDAO extends PostgresSQLDAO<Expt_Notes> {

	protected Expt_NotesDAO() {
		super(Expt_Notes.class);
	}


}
