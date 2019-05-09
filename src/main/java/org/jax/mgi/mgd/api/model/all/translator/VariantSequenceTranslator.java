package org.jax.mgi.mgd.api.model.all.translator;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.acc.domain.AccessionDomain;
import org.jax.mgi.mgd.api.model.acc.translator.AccessionTranslator;
import org.jax.mgi.mgd.api.model.all.domain.VariantSequenceDomain;
import org.jax.mgi.mgd.api.model.all.entities.VariantSequence;
import org.jax.mgi.mgd.api.util.Constants;

public class VariantSequenceTranslator extends BaseEntityDomainTranslator<VariantSequence, VariantSequenceDomain> {

	private AccessionTranslator accessionTranslator = new AccessionTranslator();
	
	@Override
	protected VariantSequenceDomain entityToDomain(VariantSequence entity) {
		VariantSequenceDomain domain = new VariantSequenceDomain();
		
		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);		
		domain.setVariantSequenceKey(String.valueOf(entity.get_variantsequence_key()));
		domain.setVariantKey(String.valueOf(entity.get_variant_key()));	
		domain.setSequenceTypeKey(String.valueOf(entity.getSequenceType().get_term_key()));
		domain.setSequenceTypeTerm(entity.getSequenceType().getTerm());
        if(entity.getStartCoordinate() != null) {
        	domain.setStartCoordinate(String.valueOf(entity.getStartCoordinate()));
        }
        if(entity.getEndCoordinate() != null) {
        	domain.setEndCoordinate(String.valueOf(entity.getEndCoordinate()));
        }
        if(entity.getReferenceSequence() != null) {
        	domain.setReferenceSequence(entity.getReferenceSequence());
        }
        if (entity.getVariantSequence() != null) {
		    domain.setVariantSequence(entity.getVariantSequence());
        }
        if(entity.getVersion() != null) {
        	domain.setVersion(entity.getVersion());
        }
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));

		// variant sequence accession ids
		if (entity.getAccessionIds() != null && !entity.getAccessionIds().isEmpty()) {
			Iterable<AccessionDomain> acc = accessionTranslator.translateEntities(entity.getAccessionIds());
			if(acc.iterator().hasNext() == true) {
				domain.setAccessionIds(IteratorUtils.toList(acc.iterator()));
			}
		}
				
		return domain;
	}

}
