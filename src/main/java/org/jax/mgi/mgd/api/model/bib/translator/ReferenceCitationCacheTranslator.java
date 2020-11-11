package org.jax.mgi.mgd.api.model.bib.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.bib.domain.ReferenceCitationCacheDomain;
import org.jax.mgi.mgd.api.model.bib.entities.Reference;

public class ReferenceCitationCacheTranslator extends BaseEntityDomainTranslator<Reference, ReferenceCitationCacheDomain> {

	@Override
	protected ReferenceCitationCacheDomain entityToDomain(Reference entity) {

		ReferenceCitationCacheDomain domain = new ReferenceCitationCacheDomain();
		
		domain.set_refs_key(entity.get_refs_key());
		domain.setNumericPart(entity.getReferenceCitationCache().getNumericPart());	
		domain.setJnumid(entity.getReferenceCitationCache().getJnumid());
		domain.setJnum(String.valueOf(entity.getReferenceCitationCache().getNumericPart()));	
		domain.setCitation(entity.getReferenceCitationCache().getCitation());
		domain.setShort_citation(entity.getReferenceCitationCache().getShort_citation());
		domain.setJournal(entity.getJournal());
		domain.setYear(String.valueOf(entity.getYear()));
		domain.setMgiid(entity.getReferenceCitationCache().getMgiid());
		domain.setPubmedid(entity.getReferenceCitationCache().getPubmedid());
		domain.setDoiid(entity.getReferenceCitationCache().getDoiid());
		domain.setReferenceType(String.valueOf(entity.getReferenceCitationCache().getReferenceType()));
		domain.setRelevanceTermKey(String.valueOf(entity.getReferenceCitationCache().get_relevance_key()));
		domain.setRelevanceTerm(entity.getReferenceCitationCache().getRelevanceTerm());
		domain.setIsReviewArticle(entity.getIsReviewArticle());
		domain.setIsReviewArticleString(String.valueOf(entity.getIsReviewArticle()));
		
		// used by validateJnumImage
		domain.setCopyright("");
		domain.setNeedsDXDOIid(false);
		domain.setIsCreativeCommons(false);

		return domain;
	}

}
