package org.jax.mgi.mgd.api.model.seq.dao;

import javax.sound.midi.Sequence;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SequenceDAO extends PostgresSQLDAO<Sequence> {
	protected SequenceDAO() {
		super(Sequence.class);
	}
}
