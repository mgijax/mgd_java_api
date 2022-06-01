package org.jax.mgi.mgd.api.model.bib.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.bib.domain.ReferenceCitationCacheDomain;
import org.jax.mgi.mgd.api.model.bib.entities.ReferenceCitationCache;
import org.jboss.logging.Logger;

public class ReferenceCitationCacheTranslator extends BaseEntityDomainTranslator<ReferenceCitationCache, ReferenceCitationCacheDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Override
	protected ReferenceCitationCacheDomain entityToDomain(ReferenceCitationCache entity) {

		ReferenceCitationCacheDomain domain = new ReferenceCitationCacheDomain();
		
		domain.setRefsKey(String.valueOf(entity.get_refs_key()));
		domain.setNumericPart(String.valueOf(entity.getNumericPart()));
		domain.setJnumid(entity.getJnumid());
		domain.setMgiid(entity.getMgiid());
		domain.setPubmedid(entity.getPubmedid());
		domain.setDoiid(entity.getDoiid());
		domain.setJournal(entity.getJournal());
		domain.setCitation(entity.getCitation());
		domain.setShort_citation(entity.getShort_citation());
		domain.setReferenceType(entity.getReferenceType());
		domain.setReferenceTypeKey(String.valueOf(entity.get_relevance_key()));
		domain.setRelevanceTerm(entity.getRelevanceTerm());
		domain.setIsReviewArticle(String.valueOf(entity.getIsReviewArticle()));
		domain.setIsReviewArticleString(entity.getIsReviewArticleString());
				
		return domain;
	}

}
