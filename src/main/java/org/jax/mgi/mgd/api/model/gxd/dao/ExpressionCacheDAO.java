package org.jax.mgi.mgd.api.model.gxd.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.gxd.entities.ExpressionCache;

public class ExpressionCacheDAO extends PostgresSQLDAO<ExpressionCache> {
	protected ExpressionCacheDAO() {
		super(ExpressionCache.class);
	}
}
