package org.jax.mgi.mgd.api.model.prb.translator;

import java.util.Comparator;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.prb.domain.ProbeAlleleDomain;
import org.jax.mgi.mgd.api.model.prb.domain.ProbeAlleleStrainDomain;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeAllele;
import org.jax.mgi.mgd.api.util.Constants;

public class ProbeAlleleTranslator extends BaseEntityDomainTranslator<ProbeAllele, ProbeAlleleDomain> {

	@Override
	protected ProbeAlleleDomain entityToDomain(ProbeAllele entity) {
		
		ProbeAlleleDomain domain = new ProbeAlleleDomain();
		
		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);	
		domain.setAlleleKey(String.valueOf(entity.get_allele_key()));				
		domain.setRflvKey(String.valueOf(entity.get_rflv_key()));	
		domain.setAllele(entity.getAllele());
		domain.setFragments(entity.getFragments());
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
		
		// strains
		if (entity.getAlleleStrains() != null && !entity.getAlleleStrains().isEmpty()) {
			ProbeAlleleStrainTranslator strainTranslator = new ProbeAlleleStrainTranslator();
			Iterable<ProbeAlleleStrainDomain> strain = strainTranslator.translateEntities(entity.getAlleleStrains());
			domain.setAlleleStrains(IteratorUtils.toList(strain.iterator()));
			domain.getAlleleStrains().sort(Comparator.comparing(ProbeAlleleStrainDomain::getStrain));						
		}
		
		return domain;
	}

}
