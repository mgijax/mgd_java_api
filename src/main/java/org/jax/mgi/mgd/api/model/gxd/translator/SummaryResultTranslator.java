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

//		private String offset;
//		private String limit;
//		private String structureID;
//		private String structure;
//		private String cellTypeID;
//		private String cellTypeKey;
//		private String cellType;
//		private String specimenLabel;
//		private String alleleDetailNote;
		
		domain.setExpressionKey(String.valueOf(entity.get_expression_key()));
		domain.setAssayKey(String.valueOf(entity.get_assay_key()));
		domain.setAssayID(entity.getAssay().getMgiAccessionIds().get(0).getAccID());
		domain.setAssayTypeKey(String.valueOf(entity.getAssayType().get_assaytype_key()));
		domain.setAssayType(entity.getAssayType().getAssayType());
		domain.setAssayTypeSequenceNum(entity.getAssayType().getSequenceNum());
		domain.setRefsKey(String.valueOf(entity.getReference().get_refs_key()));
		domain.setJnumid(entity.getReference().getJnumid());
		domain.setMarkerID(entity.getMarker().getMgiAccessionIds().get(0).getAccID());
		domain.setMarkerKey(String.valueOf(entity.get_marker_key()));
		domain.setMarkerSymbol(entity.getMarker().getSymbol());
		domain.setResultNote(entity.getResultNote());
		domain.setStrength(entity.getStrength());
		domain.setAge(entity.getAge());

//		// specimens
//		if (entity.getSpecimens() != null && !entity.getSpecimens().isEmpty()) {
//			SpecimenTranslator specimenTranslator = new SpecimenTranslator();
//			Iterable<SpecimenDomain> i = specimenTranslator.translateEntities(entity.getSpecimens());
//			domain.setSpecimens(IteratorUtils.toList(i.iterator()));
//			domain.getSpecimens().sort(Comparator.comparingInt(SpecimenDomain::getSequenceNum));
//		}
//
//		// gel lanes
//		if (entity.getGelLanes() != null && !entity.getGelLanes().isEmpty()) {
//			GelLaneTranslator gellaneTranslator = new GelLaneTranslator();
//			Iterable<GelLaneDomain> i = gellaneTranslator.translateEntities(entity.getGelLanes());
//			domain.setGelLanes(IteratorUtils.toList(i.iterator()));
//			domain.getGelLanes().sort(Comparator.comparingInt(GelLaneDomain::getSequenceNum));
//		}

//		// gel rows
//		if (entity.getGelRows() != null && !entity.getGelRows().isEmpty()) {
//			GelRowTranslator gelrowTranslator = new GelRowTranslator();
//			Iterable<GelRowDomain> i = gelrowTranslator.translateEntities(entity.getGelRows());
//			domain.setGelRows(IteratorUtils.toList(i.iterator()));
//			domain.getGelRows().sort(Comparator.comparingInt(GelRowDomain::getSequenceNum));
//		}

		return domain;
	}

}
