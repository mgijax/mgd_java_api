package org.jax.mgi.mgd.api.model.acc.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.acc.domain.AccessionReferenceDomain;
import org.jax.mgi.mgd.api.model.acc.entities.AccessionReference;

public class AccessionReferenceTranslator extends BaseEntityDomainTranslator<AccessionReference, AccessionReferenceDomain> {
	
	@Override
	protected AccessionReferenceDomain entityToDomain(AccessionReference entity, int translationDepth) {
		AccessionReferenceDomain domain = new AccessionReferenceDomain();

		domain.setAccessionKey(String.valueOf(entity.get_accession_key()));
		domain.setRefsKey(String.valueOf(entity.getReference().get_refs_key()));
		domain.setJnumid(entity.getReference().getReferenceCitationCache().getJnumid());
		domain.setJnum(String.valueOf(entity.getReference().getReferenceCitationCache().getNumericPart()));
		domain.setShort_citation(entity.getReference().getReferenceCitationCache().getShort_citation());
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));

		return domain;
	}

	@Override
	protected AccessionReference domainToEntity(AccessionReferenceDomain domain, int translationDepth) {
		// Needs to be implemented once we choose to save terms
		return null;
	}

}
