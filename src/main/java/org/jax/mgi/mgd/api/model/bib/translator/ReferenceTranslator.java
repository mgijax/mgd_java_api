package org.jax.mgi.mgd.api.model.bib.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.bib.domain.ReferenceDomain;
import org.jax.mgi.mgd.api.model.bib.entities.Reference;

public class ReferenceTranslator extends BaseEntityDomainTranslator<Reference, ReferenceDomain> {

	@Override
	protected ReferenceDomain entityToDomain(Reference entity, int translationDepth) {

		ReferenceDomain domain = new ReferenceDomain();
		
		domain.setRefsKey(String.valueOf(entity.get_refs_key()));
		domain.setPrimaryAuthor(entity.getPrimaryAuthor());
		domain.setAuthors(entity.getAuthors());
		domain.setTitle(entity.getTitle());
		domain.setJournal(entity.getJournal());
		domain.setVolume(entity.getVol());
		domain.setIssue(entity.getIssue());
		domain.setDate(entity.getDate());
		domain.setYear(String.valueOf(entity.getYear()));
		domain.setPages(entity.getPgs());
		domain.setDate(entity.getDate());
		domain.setIsReviewArticle(String.valueOf(entity.getIsReviewArticle()));
		domain.setIsDiscard(String.valueOf(entity.getIsDiscard()));
		domain.setReferenceTypeKey(String.valueOf(entity.getReferenceType().get_term_key()));
		domain.setReferenceType(entity.getReferenceType().getTerm());
		domain.setJnumID(entity.getReferenceCitationCache().getJnumid());
		domain.setJnum(String.valueOf(entity.getReferenceCitationCache().getNumericPart()));		
		domain.setShort_citation(entity.getReferenceCitationCache().getShort_citation());
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
		
		return domain;
	}

	@Override
	protected Reference domainToEntity(ReferenceDomain domain, int translationDepth) {
		// TODO Auto-generated method stub
		return null;
	}
}
