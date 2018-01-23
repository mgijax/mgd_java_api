package org.jax.mgi.mgd.api.model.gxd.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.gxd.entities.AssayNote;

@RequestScoped
public class AssayNoteDAO extends PostgresSQLDAO<AssayNote> {

	protected AssayNoteDAO() {
		super(AssayNote.class);
	}


}
