package org.jax.mgi.mgd.api.model.gxd.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.gxd.domain.AllelePairDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.AllelePair;
import org.jax.mgi.mgd.api.util.Constants;
import org.jboss.logging.Logger;

public class AllelePairTranslator extends BaseEntityDomainTranslator<AllelePair, AllelePairDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Override
	protected AllelePairDomain entityToDomain(AllelePair entity) {
		
		AllelePairDomain domain = new AllelePairDomain();
	
		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
		domain.setAllelePairKey(String.valueOf(entity.get_allelepair_key()));
		domain.setGenotypeKey(String.valueOf(entity.getGenotype().get_genotype_key()));
		domain.setAlleleKey1(String.valueOf(entity.getAllele1().get_allele_key()));
		domain.setAlleleSymbol1(String.valueOf(entity.getAllele1().getSymbol()));
		domain.setPairStateKey(String.valueOf(entity.getPairState().get_term_key()));
		domain.setPairState(String.valueOf(entity.getPairState().getTerm()));
		domain.setCompoundKey(String.valueOf(entity.getCompound().get_term_key()));
		domain.setCompound(String.valueOf(entity.getCompound().getTerm()));
		domain.setSequenceNum(String.valueOf(entity.getSequenceNum()));		
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
	
		// may be null
		if (entity.getAllele2() != null) {
			domain.setAlleleKey2(String.valueOf(entity.getAllele2().get_allele_key()));
			domain.setAlleleSymbol2(String.valueOf(entity.getAllele2().getSymbol()));
		}

		if (entity.getMarker() != null) {
			domain.setMarkerKey(String.valueOf(entity.getMarker().get_marker_key()));
			domain.setMarkerSymbol(String.valueOf(entity.getMarker().getSymbol()));
			domain.setMarkerChromosome(entity.getMarker().getChromosome());
		}	
		
		// may be null
		if (entity.getCellLine1() != null) {
			domain.setCellLineKey1(String.valueOf(entity.getCellLine1().get_cellline_key()));
			domain.setCellLine1(entity.getCellLine1().getCellLine());
		}
		
		// may be null
		if (entity.getCellLine2() != null) {
			domain.setCellLineKey2(String.valueOf(entity.getCellLine2().get_cellline_key()));
			domain.setCellLine2(entity.getCellLine2().getCellLine());
		}
				
		return domain;
	}

}
