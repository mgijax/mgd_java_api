package org.jax.mgi.mgd.api.model.gxd.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.gxd.domain.InSituResultStructureDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.InSituResultStructure;
import org.jax.mgi.mgd.api.util.Constants;
import org.jboss.logging.Logger;

public class InSituResultStructureTranslator extends BaseEntityDomainTranslator<InSituResultStructure, InSituResultStructureDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Override
	protected InSituResultStructureDomain entityToDomain(InSituResultStructure entity) {

		InSituResultStructureDomain domain = new InSituResultStructureDomain();

		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
		domain.setResultStructureKey(String.valueOf(entity.get_resultstructure_key()));				
		domain.setResultKey(String.valueOf(entity.get_result_key()));				
		domain.setEmapaTermKey(String.valueOf(entity.getEmapaTerm().get_term_key()));
		domain.setEmapaTerm(entity.getEmapaTerm().getTerm());
		domain.setTheilerStageKey(String.valueOf(entity.getTheilerStage().get_stage_key()));
		domain.setTheilerStage(String.valueOf(entity.getTheilerStage().getStage()));	
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
		
		return domain;
	}

}
