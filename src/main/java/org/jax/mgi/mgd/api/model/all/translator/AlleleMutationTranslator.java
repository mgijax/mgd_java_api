package org.jax.mgi.mgd.api.model.all.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.all.domain.AlleleMutationDomain;
import org.jax.mgi.mgd.api.model.all.entities.AlleleMutation;
import org.jax.mgi.mgd.api.util.Constants;

public class AlleleMutationTranslator extends BaseEntityDomainTranslator<AlleleMutation, AlleleMutationDomain> {
	
	@Override
	protected AlleleMutationDomain entityToDomain(AlleleMutation entity) {
		
		AlleleMutationDomain domain = new AlleleMutationDomain();
		
		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
		domain.setAlleleKey(String.valueOf(entity.getAllele().get_allele_key()));
		domain.setMutationKey(String.valueOf(entity.getMutation().get_term_key()));
		domain.setMutation(entity.getMutation().getTerm());
//		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
//		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
		
		return domain;
	}
	
}
