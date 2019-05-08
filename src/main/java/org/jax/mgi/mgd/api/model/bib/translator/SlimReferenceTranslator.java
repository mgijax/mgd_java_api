package org.jax.mgi.mgd.api.model.bib.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.bib.domain.SlimReferenceDomain;
import org.jax.mgi.mgd.api.model.bib.entities.Reference;

public class SlimReferenceTranslator extends BaseEntityDomainTranslator<Reference, SlimReferenceDomain> {

	@Override
	protected SlimReferenceDomain entityToDomain(Reference entity, int translationDepth) {

		SlimReferenceDomain domain = new SlimReferenceDomain();
		
		domain.setRefsKey(String.valueOf(entity.get_refs_key()));
		domain.setJnumID(entity.getReferenceCitationCache().getJnumid());
		domain.setJnum(String.valueOf(entity.getReferenceCitationCache().getNumericPart()));	
		domain.setShort_citation(entity.getReferenceCitationCache().getShort_citation());
		
		// used by validateJnumCopyright
		domain.setCopyright("");
		domain.setNeedsDXDOIid(false);
		
		return domain;
	}

}
