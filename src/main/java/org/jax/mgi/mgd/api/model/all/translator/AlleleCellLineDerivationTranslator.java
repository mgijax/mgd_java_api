package org.jax.mgi.mgd.api.model.all.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.all.domain.AlleleCellLineDerivationDomain;
import org.jax.mgi.mgd.api.model.all.entities.AlleleCellLineDerivation;
import org.jax.mgi.mgd.api.util.Constants;

public class AlleleCellLineDerivationTranslator extends BaseEntityDomainTranslator<AlleleCellLineDerivation, AlleleCellLineDerivationDomain> {
	
	@Override
	protected AlleleCellLineDerivationDomain entityToDomain(AlleleCellLineDerivation entity) {
		
		AlleleCellLineDerivationDomain domain = new AlleleCellLineDerivationDomain();
		
		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
		domain.setDerivationKey(String.valueOf(entity.get_derivation_key()));
		domain.setName(entity.getName());
		domain.setDescription(entity.getDescription());
		domain.setVectorKey(String.valueOf(entity.getVector().get_term_key()));
		domain.setVector(entity.getVector().getTerm());
		domain.setVectorTypeKey(String.valueOf(entity.getVectorType().get_term_key()));
		domain.setVectorType(entity.getVectorType().getTerm());
		domain.setParentCellLineKey(String.valueOf(entity.getParentCellLine().get_cellline_key()));
		domain.setParentCellLine(entity.getParentCellLine().getCellLine());
		domain.setCreatorKey(String.valueOf(entity.getCreator().get_term_key()));
		domain.setCreator(entity.getCreator().getTerm());
		domain.setDerivationTypeKey(String.valueOf(entity.getDerivationType().get_term_key()));
		domain.setDerivationType(entity.getDerivationType().getTerm());
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
		
//		if (entity.getParentCellLine() != null) {
//			domain.setParentStrainKey(String.valueOf(entity.getParentCellLine().getStrain().get_strain_key()));
//			domain.setParentStrain(entity.getParentCellLine().getStrain().getStrain());
//		}
		
		return domain;
	}
	
}
