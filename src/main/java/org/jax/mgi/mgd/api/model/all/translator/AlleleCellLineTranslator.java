package org.jax.mgi.mgd.api.model.all.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.all.domain.AlleleCellLineDomain;
import org.jax.mgi.mgd.api.model.all.entities.AlleleCellLine;
import org.jax.mgi.mgd.api.util.Constants;

public class AlleleCellLineTranslator extends BaseEntityDomainTranslator<AlleleCellLine, AlleleCellLineDomain> {
	
	@Override
	protected AlleleCellLineDomain entityToDomain(AlleleCellLine entity) {
		
		AlleleCellLineDomain domain = new AlleleCellLineDomain();
		
		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
		domain.setAssocKey(String.valueOf(entity.get_assoc_key()));
		domain.setAlleleKey(String.valueOf(entity.get_allele_key()));
		domain.setMutantCellLineKey(String.valueOf(entity.getMutantCellLine().get_cellline_key()));
		domain.setMutantCellLine(entity.getMutantCellLine().getCellLine());
		domain.setIsMutant(String.valueOf(entity.getMutantCellLine().getIsMutant()));
		domain.setCellLineTypeKey(String.valueOf(entity.getMutantCellLine().getCellLineType().get_term_key()));
		domain.setCellLineType(entity.getMutantCellLine().getCellLineType().getTerm());
		domain.setStrainKey(String.valueOf(entity.getMutantCellLine().getStrain().get_strain_key()));
		domain.setStrain(entity.getMutantCellLine().getStrain().getStrain());
		domain.setDerivationKey(String.valueOf(entity.getMutantCellLine().getDerivation().get_derivation_key()));
		domain.setVectorKey(String.valueOf(entity.getMutantCellLine().getDerivation().getVector().get_term_key()));
		domain.setVector(entity.getMutantCellLine().getDerivation().getVector().getTerm());
		domain.setParentCellLineKey(String.valueOf(entity.getMutantCellLine().getDerivation().getParentCellLine().get_cellline_key()));
		domain.setParentCellLine(entity.getMutantCellLine().getDerivation().getParentCellLine().getCellLine());
		domain.setCreatorKey(String.valueOf(entity.getMutantCellLine().getDerivation().getCreator().get_term_key()));
		domain.setCreator(entity.getMutantCellLine().getDerivation().getCreator().getTerm());
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
		
		return domain;
	}
	
}
