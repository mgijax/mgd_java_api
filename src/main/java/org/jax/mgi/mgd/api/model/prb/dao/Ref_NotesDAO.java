package org.jax.mgi.mgd.api.model.prb.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.prb.entities.Ref_Notes;

@RequestScoped
public class Ref_NotesDAO extends PostgresSQLDAO<Ref_Notes> {

	protected Ref_NotesDAO() {
		super(Ref_Notes.class);
	}


}
