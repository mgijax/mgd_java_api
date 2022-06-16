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
		
		domain.setGenotypeKey(String.valueOf(entity.getGenotypeKey()));
		domain.setAnnotKey(String.valueOf(entity.getAnnotKey()));
		domain.setTermKey(String.valueOf(entity.getTermKey()));
		domain.setTerm(entity.getTerm());
		domain.setHeaderTermKey(String.valueOf(entity.getHeaderTermKey()));
		domain.setHeaderTerm(entity.getHeaderTerm());
		
		return domain;
	}

}
