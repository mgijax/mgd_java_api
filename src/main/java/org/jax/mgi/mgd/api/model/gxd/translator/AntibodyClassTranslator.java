package org.jax.mgi.mgd.api.model.gxd.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.gxd.domain.AntibodyClassDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.AntibodyClass;
import org.jax.mgi.mgd.api.util.Constants;
import org.jboss.logging.Logger;

public class AntibodyClassTranslator extends BaseEntityDomainTranslator<AntibodyClass, AntibodyClassDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Override
	protected AntibodyClassDomain entityToDomain(AntibodyClass entity) {

		AntibodyClassDomain domain = new AntibodyClassDomain();

		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
		domain.setTermKey(String.valueOf(entity.get_antibodyclass_key()));
		domain.setTerm(entity.getAntibodyClass());
		domain.setVocabKey("151");
//		domain.setAbbreviation(null);
//		domain.setNote(null);
//		domain.setSequenceNum(null);
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
		
		return domain;
	}

}
