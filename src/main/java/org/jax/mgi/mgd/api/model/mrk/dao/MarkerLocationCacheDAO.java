package org.jax.mgi.mgd.api.model.mrk.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.mrk.entities.MarkerLocationCache;

public class MarkerLocationCacheDAO extends PostgresSQLDAO<MarkerLocationCache> {
	protected MarkerLocationCacheDAO() {
		super(MarkerLocationCache.class);
	}
}
