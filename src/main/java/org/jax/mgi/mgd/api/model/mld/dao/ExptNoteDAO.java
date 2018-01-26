package org.jax.mgi.mgd.api.model.mld.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mld.entities.ExptNotes;

@RequestScoped
public class ExptNotesDAO extends PostgresSQLDAO<ExptNotes> {

	protected ExptNotesDAO() {
		super(ExptNotes.class);
	}


}
