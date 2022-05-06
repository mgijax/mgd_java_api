package org.jax.mgi.mgd.api.model.bib.dao;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.bib.entities.LTReference;

public class LTReferenceDAO extends PostgresSQLDAO<LTReference> {
	protected LTReferenceDAO() {
		super(LTReference.class);
	}
}
