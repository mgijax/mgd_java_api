package org.jax.mgi.mgd.api.model.mgi.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mgi.entities.NoteChunk;

@RequestScoped
public class NoteChunkDAO extends PostgresSQLDAO<NoteChunk> {

	public NoteChunkDAO() {
		super(NoteChunk.class);
	}

}
