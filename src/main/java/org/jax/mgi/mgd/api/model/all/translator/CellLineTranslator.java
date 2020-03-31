package org.jax.mgi.mgd.api.model.all.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.all.domain.CellLineDomain;
import org.jax.mgi.mgd.api.model.all.entities.CellLine;

public class CellLineTranslator extends BaseEntityDomainTranslator<CellLine, CellLineDomain> {
	
	@Override
	protected CellLineDomain entityToDomain(CellLine entity) {
		
		CellLineDomain domain = new CellLineDomain();
		
		domain.setCellLineKey(String.valueOf(entity.get_cellline_key()));
		domain.setCellLine(entity.getCellLine());
		domain.setIsMutant(String.valueOf(entity.getIsMutant()));
		domain.setCellLineTypeKey(String.valueOf(entity.getCellLineType().get_term_key()));
		domain.setCellLineType(entity.getCellLineType().getTerm());
		domain.setStrainKey(String.valueOf(entity.getStrain().get_strain_key()));
		domain.setStrain(entity.getStrain().getStrain());
		domain.setDerivationKey(String.valueOf(entity.getDerivation().get_derivation_key()));
		domain.setCreator(entity.getDerivation().getCellLineCreator().getTerm());
		domain.setParentCellLineKey(String.valueOf(entity.getDerivation().getParentCellLine().get_cellline_key()));
		domain.setParentCellLine(entity.getDerivation().getParentCellLine().getCellLine());
		domain.setParentStrainKey(String.valueOf(entity.getDerivation().getParentCellLine().getStrain().get_strain_key()));
		domain.setParentStrain(entity.getDerivation().getParentCellLine().getStrain().getStrain());		
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
		
		return domain;
	}
	
}
