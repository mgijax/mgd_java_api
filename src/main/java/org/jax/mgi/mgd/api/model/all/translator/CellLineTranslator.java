package org.jax.mgi.mgd.api.model.all.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.all.domain.AlleleCellLineDerivationDomain;
import org.jax.mgi.mgd.api.model.all.domain.CellLineDomain;
import org.jax.mgi.mgd.api.model.all.entities.CellLine;
import org.jax.mgi.mgd.api.util.Constants;

public class CellLineTranslator extends BaseEntityDomainTranslator<CellLine, CellLineDomain> {
	
	@Override
	protected CellLineDomain entityToDomain(CellLine entity) {
		
		CellLineDomain domain = new CellLineDomain();

		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
		domain.setCellLineKey(String.valueOf(entity.get_cellline_key()));
		domain.setCellLine(entity.getCellLine());
		domain.setIsMutant(String.valueOf(entity.getIsMutant()));
		domain.setCellLineTypeKey(String.valueOf(entity.getCellLineType().get_term_key()));
		domain.setCellLineType(entity.getCellLineType().getTerm());
		domain.setStrainKey(String.valueOf(entity.getStrain().get_strain_key()));
		domain.setStrain(entity.getStrain().getStrain());		
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));

		if (entity.getDerivation() != null) {
			AlleleCellLineDerivationTranslator derivationTranslator = new AlleleCellLineDerivationTranslator();
			AlleleCellLineDerivationDomain derivation = derivationTranslator.translate(entity.getDerivation());
			domain.setDerivation(derivation);
		}
		
		// if cell line type = embryonic stem cell...
		if (entity.getCellLineType().get_term_key() == 3982968) {
			domain.setCellLineDisplay(String.valueOf(entity.getCellLine() + ';' + entity.getStrain().getStrain()));			
		}
		else {
			domain.setCellLineDisplay(String.valueOf(entity.getCellLine() + ';' + entity.getStrain().getStrain() + ';' + entity.getCellLineType().getTerm()));						
		}
		
		return domain;
	}
	
}
