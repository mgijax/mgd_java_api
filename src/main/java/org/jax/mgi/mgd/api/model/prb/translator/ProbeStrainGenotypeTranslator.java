package org.jax.mgi.mgd.api.model.prb.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.prb.domain.ProbeStrainGenotypeDomain;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeStrainGenotype;
import org.jax.mgi.mgd.api.util.Constants;
import org.jboss.logging.Logger;

public class ProbeStrainGenotypeTranslator extends BaseEntityDomainTranslator<ProbeStrainGenotype, ProbeStrainGenotypeDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Override
	protected ProbeStrainGenotypeDomain entityToDomain(ProbeStrainGenotype entity) {
		
		ProbeStrainGenotypeDomain domain = new ProbeStrainGenotypeDomain();
		
		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
		domain.setStrainMarkerKey(String.valueOf(entity.get_straingenotype_key()));		
		domain.setStrainKey(String.valueOf(entity.get_strain_key()));
		domain.setGenotypeKey(String.valueOf(entity.getGenotype().get_genotype_key()));
		domain.setGenotypeDisplay(entity.getGenotype().getStrain().getStrain());
		domain.setGenotypeAccId(entity.getGenotype().getMgiAccessionIds().get(0).getAccID());
		domain.setQualifierKey(String.valueOf(entity.getQualifier().get_term_key()));
		domain.setQualifierTerm(entity.getQualifier().getTerm());
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));	
		
		return domain;
	}

}
