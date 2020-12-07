package org.jax.mgi.mgd.api.model.prb.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.prb.domain.ProbeStrainMarkerDomain;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeStrainMarker;
import org.jax.mgi.mgd.api.util.Constants;
import org.jboss.logging.Logger;

public class ProbeStrainMarkerTranslator extends BaseEntityDomainTranslator<ProbeStrainMarker, ProbeStrainMarkerDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Override
	protected ProbeStrainMarkerDomain entityToDomain(ProbeStrainMarker entity) {
		
		ProbeStrainMarkerDomain domain = new ProbeStrainMarkerDomain();
		
		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
		domain.setStrainMarkerKey(String.valueOf(entity.get_strainmarker_key()));		
		domain.setStrainKey(String.valueOf(entity.get_strain_key()));
		domain.setMarkerKey(String.valueOf(entity.getMarker().get_marker_key()));
		domain.setMarkerSymbol(entity.getMarker().getSymbol());
		domain.setChromosome(entity.getMarker().getChromosome());
		domain.setQualifierKey(String.valueOf(entity.getQualifier().get_term_key()));
		domain.setQualifierTerm(entity.getQualifier().getTerm());
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));	
		
		if (entity.getAllele() != null) {
			domain.setAlleleKey(String.valueOf(entity.getAllele().get_allele_key()));
			domain.setAlleleSymbol(entity.getAllele().getSymbol());
			domain.setStrainOfOrigin(entity.getAllele().getStrain().getStrain());			
		}
				
		return domain;
	}

}
