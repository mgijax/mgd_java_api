package org.jax.mgi.mgd.api.model.seq.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.seq.entities.SeqMarkerCache;

public class SeqMarkerCacheDAO extends PostgresSQLDAO<SeqMarkerCache> {
	protected SeqMarkerCacheDAO() {
		super(SeqMarkerCache.class);
	}
}
