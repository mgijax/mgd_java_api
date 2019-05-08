package org.jax.mgi.mgd.api.model.seq.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.seq.domain.SequenceDomain;
import org.jax.mgi.mgd.api.model.seq.entities.Sequence;

public class SequenceTranslator extends BaseEntityDomainTranslator<Sequence, SequenceDomain> {

	@Override
	protected SequenceDomain entityToDomain(Sequence entity, int translationDepth) {
		
		SequenceDomain domain = new SequenceDomain();
		domain.set_sequence_key(entity.get_sequence_key());
		domain.setLength(entity.getLength());
		domain.setDescription(entity.getDescription());
		domain.setVersion(entity.getVersion());
		domain.setDivision(entity.getDivision());
		domain.setVirtual(entity.getVirtual());
		domain.setNumberOfOrganisms(entity.getNumberOfOrganisms());
		domain.setSeqrecord_date(entity.getSeqrecord_date());
		domain.setSequence_date(entity.getSequence_date());
		domain.setCreation_date(entity.getCreation_date());
		domain.setModification_date(entity.getModification_date());
		
		if(translationDepth > 0) {
			// load relationships
		}
		return domain;
	}

}
