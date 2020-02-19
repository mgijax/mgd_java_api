package org.jax.mgi.mgd.api.model.gxd.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.gxd.domain.AntibodyPrepDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.AntibodyPrep;
import org.jax.mgi.mgd.api.util.Constants;
import org.jboss.logging.Logger;

public class AntibodyPrepTranslator extends BaseEntityDomainTranslator<AntibodyPrep, AntibodyPrepDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Override
	protected AntibodyPrepDomain entityToDomain(AntibodyPrep entity) {

		AntibodyPrepDomain domain = new AntibodyPrepDomain();
		
		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);				
		domain.setAntibodyPrepKey(String.valueOf(entity.get_antibodyprep_key()));
		domain.setAntibodyKey(String.valueOf(entity.getAntibody().get_antibody_key()));
		domain.setAntibodyName(entity.getAntibody().getAntibodyName());
		domain.setAntibodyAccID(entity.getAntibody().getMgiAccessionIds().get(0).getAccID());
		domain.setSecondaryKey(String.valueOf(entity.getSecondary().get_secondary_key()));
		domain.setSecondaryName(entity.getSecondary().getSecondary());
		domain.setLabelKey(String.valueOf(entity.getLabel().get_label_key()));
		domain.setLabelName(entity.getLabel().getLabel());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));		

		return domain;
	}

}
