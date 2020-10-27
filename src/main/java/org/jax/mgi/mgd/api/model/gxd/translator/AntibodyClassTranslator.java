package org.jax.mgi.mgd.api.model.gxd.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.gxd.domain.AntibodyClassDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.AntibodyClass;
import org.jboss.logging.Logger;

public class AntibodyClassTranslator extends BaseEntityDomainTranslator<AntibodyClass, AntibodyClassDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Override
	protected AntibodyClassDomain entityToDomain(AntibodyClass entity) {

		AntibodyClassDomain domain = new AntibodyClassDomain();

		domain.setVocabKey("151");
		domain.setIsSimple(1);
		domain.setIsPrivate(0);
		domain.setName(entity.getAntibodyClass());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
			
//		Iterable<TermDomain> terms = termTranslator.translateEntities(entity.getTerms());
//		domain.setTerms(IteratorUtils.toList(terms.iterator()));
		
		return domain;
	}

}
