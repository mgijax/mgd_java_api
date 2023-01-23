package org.jax.mgi.mgd.api.model.gxd.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.gxd.domain.SummaryResultDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.ExpressionCache;
import org.jboss.logging.Logger;

public class SummaryResultTranslator extends BaseEntityDomainTranslator<ExpressionCache, SummaryResultDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Override
	protected SummaryResultDomain entityToDomain(ExpressionCache entity) {

		SummaryResultDomain domain = new SummaryResultDomain();
		
		domain.setExpressionKey(String.valueOf(entity.get_expression_key()));
		domain.setStageKey(entity.get_stage_key());
		domain.setAssayKey(String.valueOf(entity.getAssay().get_assay_key()));
		domain.setAssayID(entity.getAssay().getMgiAccessionIds().get(0).getAccID());
		domain.setAssayTypeKey(String.valueOf(entity.getAssayType().get_assaytype_key()));
		domain.setAssayType(entity.getAssayType().getAssayType());
		domain.setAssayTypeSequenceNum(entity.getAssayType().getSequenceNum());
		domain.setRefsKey(String.valueOf(entity.getReference().get_refs_key()));
		domain.setJnumid(entity.getReference().getJnumid());
		domain.setMarkerID(entity.getMarker().getMgiAccessionIds().get(0).getAccID());
		domain.setMarkerKey(String.valueOf(entity.getMarker().get_marker_key()));
		domain.setMarkerSymbol(entity.getMarker().getSymbol());
		domain.setResultNote(entity.getResultNote());
		domain.setStrength(entity.getStrength());
		domain.setAge(entity.getAge());
		domain.setStructureID(entity.getEmapaTerm().getAccessionIds().get(0).getAccID());
		domain.setStructure("TS" + String.valueOf(entity.get_stage_key()) + ":" + entity.getEmapaTerm().getTerm());
		
		if (entity.getSpecimen() != null) {
			domain.setSpecimenLabel(entity.getSpecimen().getSpecimenLabel());
		}
		else {
			domain.setSpecimenLabel("");
		}
		
		if (entity.getCellTypeTerm() != null) {
			domain.setCellTypeID(entity.getCellTypeTerm().getAccessionIds().get(0).getAccID());
			domain.setCellTypeKey(String.valueOf(entity.getCellTypeTerm().get_term_key()));
			domain.setCellType(entity.getCellTypeTerm().getTerm());
		}
		else {
			domain.setCellType("");
		}
		
		if (entity.getGenotype().getAlleleDetailNote() != null) {
			if (entity.getGenotype().getAlleleDetailNote().size() > 0) {
				domain.setAlleleDetailNote(entity.getGenotype().getAlleleDetailNote().get(0).getNote());
			}
		}

		return domain;
	}

}
