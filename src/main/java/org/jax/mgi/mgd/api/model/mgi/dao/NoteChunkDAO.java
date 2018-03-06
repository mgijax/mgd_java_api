package org.jax.mgi.mgd.api.model.mgi.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mgi.entities.NoteChunk;

public class NoteChunkDAO extends PostgresSQLDAO<NoteChunk> {
	public NoteChunkDAO() {
		super(NoteChunk.class);
	}
}
