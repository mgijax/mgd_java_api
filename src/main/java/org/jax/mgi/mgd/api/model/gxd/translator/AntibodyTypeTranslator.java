package org.jax.mgi.mgd.api.model.gxd.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.gxd.domain.AntibodyTypeDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.AntibodyType;
import org.jboss.logging.Logger;

public class AntibodyTypeTranslator extends BaseEntityDomainTranslator<AntibodyType, AntibodyTypeDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Override
	protected AntibodyTypeDomain entityToDomain(AntibodyType entity) {

		AntibodyTypeDomain domain = new AntibodyTypeDomain();

		domain.setAntibodyTypeKey(String.valueOf(entity.get_antibodytype_key()));
		domain.setAntibodyType(entity.getAntibodyType());
		
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
		
		return domain;
	}

}
