package org.jax.mgi.mgd.api.model.gxd.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.gxd.entities.Pattern;

public class PatternDAO extends PostgresSQLDAO<Pattern> {
	protected PatternDAO() {
		super(Pattern.class);
	}
}
