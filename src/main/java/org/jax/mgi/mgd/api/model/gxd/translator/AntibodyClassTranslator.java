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

		domain.setAntibodyClassKey(String.valueOf(entity.get_antibodyclass_key()));
		domain.setAntibodyClass(entity.getAntibodyClass());
		
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
		
		return domain;
	}

}
