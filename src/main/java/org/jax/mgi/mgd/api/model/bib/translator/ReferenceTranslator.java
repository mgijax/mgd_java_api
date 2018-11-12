package org.jax.mgi.mgd.api.model.bib.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.bib.domain.ReferenceDomain;
import org.jax.mgi.mgd.api.model.bib.entities.Reference;

public class ReferenceTranslator extends BaseEntityDomainTranslator<Reference, ReferenceDomain> {

	@Override
	protected ReferenceDomain entityToDomain(Reference entity, int translationDepth) {
		if (entity == null) { return null; }
		ReferenceDomain domain = new ReferenceDomain();
		domain.set_refs_key(entity.get_refs_key());
		domain.setPrimaryAuthor(entity.getPrimaryAuthor());
		domain.setAuthors(entity.getAuthors());
		domain.setTitle(entity.getTitle());
		domain.setJournal(entity.getJournal());
		domain.setVolume(entity.getVol());
		domain.setIssue(entity.getIssue());
		domain.setDate(entity.getDate());
		domain.setYear(entity.getYear());
		domain.setPages(entity.getPgs());
		domain.setDate(entity.getDate());
		domain.setIsReviewArticle(entity.getIsReviewArticle());
		domain.setIsDiscard(entity.getIsDiscard());
		domain.setReferenceTypeKey(entity.getReferenceType().get_term_key().toString());
		domain.setReferenceType(entity.getReferenceType().getTerm());
		domain.setJnumID(entity.getReferenceCitationCache().getJnumid());
		domain.setShort_citation(entity.getReferenceCitationCache().getShort_citation());
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setCreatedBy(entity.getCreatedBy().getName());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setModifiedBy(entity.getModifiedBy().getName());
		domain.setCreation_date(entity.getCreation_date());
		domain.setModification_date(entity.getModification_date());
		
		if(translationDepth > 0) {
			// load relationships
		}
		return domain;
	}

	@Override
	protected Reference domainToEntity(ReferenceDomain domain, int translationDepth) {
		// TODO Auto-generated method stub
		return null;
	}
}
