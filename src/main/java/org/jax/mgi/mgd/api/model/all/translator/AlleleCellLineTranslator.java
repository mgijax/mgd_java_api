package org.jax.mgi.mgd.api.model.all.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.all.domain.AlleleCellLineDomain;
import org.jax.mgi.mgd.api.model.all.domain.CellLineDomain;
import org.jax.mgi.mgd.api.model.all.entities.AlleleCellLine;
import org.jax.mgi.mgd.api.util.Constants;

public class AlleleCellLineTranslator extends BaseEntityDomainTranslator<AlleleCellLine, AlleleCellLineDomain> {
	
	@Override
	protected AlleleCellLineDomain entityToDomain(AlleleCellLine entity) {
		
		AlleleCellLineDomain domain = new AlleleCellLineDomain();
		
		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
		domain.setAssocKey(String.valueOf(entity.get_assoc_key()));
		domain.setAlleleKey(String.valueOf(entity.get_allele_key()));
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));

		if (entity.getMutantCellLine() != null) {
			CellLineTranslator cellLineTranslator = new CellLineTranslator();
			CellLineDomain mutationCellLine = cellLineTranslator.translate(entity.getMutantCellLine());
			domain.setMutantCellLine(mutationCellLine);				
			CellLineDomain parentCellLine = cellLineTranslator.translate(entity.getMutantCellLine().getDerivation().getParentCellLine());
			domain.setParentCellLine(parentCellLine);				
		}
		
		
		return domain;
	}
	
}
