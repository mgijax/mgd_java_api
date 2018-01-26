package org.jax.mgi.mgd.api.model.mld.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mld.entities.MLDNote;

@RequestScoped
public class MLDNoteDAO extends PostgresSQLDAO<MLDNote> {

	protected MLDNoteDAO() {
		super(MLDNote.class);
	}


}
