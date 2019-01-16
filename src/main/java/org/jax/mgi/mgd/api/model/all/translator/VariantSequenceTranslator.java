package org.jax.mgi.mgd.api.model.all.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.all.domain.VariantSequenceDomain;
import org.jax.mgi.mgd.api.model.all.entities.VariantSequence;
import org.jax.mgi.mgd.api.util.Constants;

public class VariantSequenceTranslator extends BaseEntityDomainTranslator<VariantSequence, VariantSequenceDomain> {
	
	@Override
	protected VariantSequenceDomain entityToDomain(VariantSequence entity, int translationDepth) {
		VariantSequenceDomain domain = new VariantSequenceDomain();
		
		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);		
		domain.setVariantSequenceKey(String.valueOf(entity.get_variantsequence_key()));
		domain.setVariantKey(String.valueOf(entity.getVariant().get_variant_key()));	
		domain.setSequenceTypeKey(String.valueOf(entity.getSequenceType().get_term_key()));
		domain.setSequenceTypeTerm(entity.getSequenceType().getTerm());
        domain.setStartCoordinate(entity.getStartCoordinate());
        domain.setEndCoordinate(entity.getEndCoordinate());
		domain.setReferenceSequence(entity.getReferenceSequence());
		domain.setVariantSequence(entity.getVariantSequence());
		domain.setVersion(entity.getVersion());
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
		
		return domain;
	}

	@Override
	protected VariantSequence domainToEntity(VariantSequenceDomain domain, int translationDepth) {
		// TODO Auto-generated method stub
		return null;
	}

}
