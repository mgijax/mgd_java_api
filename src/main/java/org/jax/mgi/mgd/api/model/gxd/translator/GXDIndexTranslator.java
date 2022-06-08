package org.jax.mgi.mgd.api.model.gxd.translator;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.gxd.domain.GXDIndexDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.GXDIndexStageDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.GXDIndex;
import org.jboss.logging.Logger;

public class GXDIndexTranslator extends BaseEntityDomainTranslator<GXDIndex, GXDIndexDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Override
	protected GXDIndexDomain entityToDomain(GXDIndex entity) {

		GXDIndexDomain domain = new GXDIndexDomain();

		domain.setIndexKey(String.valueOf(entity.get_index_key()));
		domain.setComments(entity.getComments());
		domain.setPriorityKey(String.valueOf(entity.getPriority().get_term_key()));
		domain.setPriority(entity.getPriority().getTerm());
		domain.setConditionalMutantsKey(String.valueOf(entity.getConditionalMutants().get_term_key()));
		domain.setConditionalMutants(entity.getConditionalMutants().getTerm());
		domain.setRefsKey(String.valueOf(entity.getReference().get_refs_key()));
		domain.setJnumid(entity.getReference().getJnumid());
		domain.setJnum(String.valueOf(entity.getReference().getNumericPart()));
		domain.setShort_citation(entity.getReference().getShort_citation());
		domain.setMarkerKey(String.valueOf(entity.getMarker().get_marker_key()));
		domain.setMarkerSymbol(entity.getMarker().getSymbol());		
		domain.setMarkerName(entity.getMarker().getName());
		domain.setMarkerChromosome(entity.getMarker().getChromosome());
		domain.setMarkerAccID(entity.getMarker().getMgiAccessionIds().get(0).getAccID());
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));

		// stages
		log.info("start stages");
		if (entity.getIndexStages() != null || !entity.getIndexStages().isEmpty()) {
			GXDIndexStageTranslator stageTranslator = new GXDIndexStageTranslator();
			Iterable<GXDIndexStageDomain> i = stageTranslator.translateEntities(entity.getIndexStages());
			domain.setIndexStages(IteratorUtils.toList(i.iterator()));
		}
		log.info("end stages");

		// is the reference full-code/exists in expression cache
//		if (entity.getExpressionCache() != null || !entity.getExpressionCache().isEmpty()) {
//			domain.setIsFullCoded("1");
//		}
//		else {
//			domain.setIsFullCoded("0");
//		}
		
		return domain;
	}

}
