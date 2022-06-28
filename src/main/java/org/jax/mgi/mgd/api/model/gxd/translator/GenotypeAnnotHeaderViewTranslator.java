package org.jax.mgi.mgd.api.model.gxd.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.gxd.domain.GenotypeAnnotHeaderViewDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.GenotypeAnnotHeaderView;
import org.jboss.logging.Logger;

public class GenotypeAnnotHeaderViewTranslator extends BaseEntityDomainTranslator<GenotypeAnnotHeaderView, GenotypeAnnotHeaderViewDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Override
	protected GenotypeAnnotHeaderViewDomain entityToDomain(GenotypeAnnotHeaderView entity) {
		
		GenotypeAnnotHeaderViewDomain domain = new GenotypeAnnotHeaderViewDomain();

		domain.setHeaderTermKey(String.valueOf(entity.getHeaderTermKey()));		
		domain.setAnnotKey(String.valueOf(entity.getAnnotKey()));
		domain.setTermKey(String.valueOf(entity.getTermKey()));
		domain.setTerm(entity.getTerm());
		domain.setTermSequenceNum(entity.getTermSequenceNum());
		domain.setHeaderTerm(entity.getHeaderTerm());
		domain.setHeaderSequenceNum(entity.getHeaderSequenceNum());
		return domain;
	}

}
