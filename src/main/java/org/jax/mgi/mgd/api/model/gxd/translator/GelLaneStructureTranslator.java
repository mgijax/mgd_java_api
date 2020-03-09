package org.jax.mgi.mgd.api.model.gxd.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.gxd.domain.GelLaneStructureDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.GelLaneStructure;
import org.jax.mgi.mgd.api.util.Constants;
import org.jboss.logging.Logger;

public class GelLaneStructureTranslator extends BaseEntityDomainTranslator<GelLaneStructure, GelLaneStructureDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Override
	protected GelLaneStructureDomain entityToDomain(GelLaneStructure entity) {

		GelLaneStructureDomain domain = new GelLaneStructureDomain();

		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
		domain.setGelLaneStructureKey(String.valueOf(entity.get_gellanestructure_key()));				
		domain.setGelLaneKey(String.valueOf(entity.get_gellane_key()));				
		domain.setEmapaTermKey(String.valueOf(entity.getEmapaTerm().get_term_key()));
		domain.setEmapaTerm(entity.getEmapaTerm().getTerm());
		domain.setTheilerStageKey(String.valueOf(entity.getTheilerStage().get_stage_key()));
		domain.setTheilerStage(String.valueOf(entity.getTheilerStage().getStage()));	
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
		
		return domain;
	}

}
