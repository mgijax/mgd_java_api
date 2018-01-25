package org.jax.mgi.mgd.api.model.seq.dao;

import javax.enterprise.context.RequestScoped;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.model.seq.entities.SequenceRaw;

@RequestScoped
public class SequenceRawDAO extends PostgresSQLDAO<SequenceRaw> {

	protected SequenceRawDAO() {
		super(SequenceRaw.class);
	}


}
