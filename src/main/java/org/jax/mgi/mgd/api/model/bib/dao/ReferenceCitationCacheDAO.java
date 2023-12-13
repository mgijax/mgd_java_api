package org.jax.mgi.mgd.api.model.bib.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.bib.entities.ReferenceCitationCache;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ReferenceCitationCacheDAO extends PostgresSQLDAO<ReferenceCitationCache> {
	protected ReferenceCitationCacheDAO() {
		super(ReferenceCitationCache.class);
	}
}
