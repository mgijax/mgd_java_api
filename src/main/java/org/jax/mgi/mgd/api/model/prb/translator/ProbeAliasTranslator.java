package org.jax.mgi.mgd.api.model.prb.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.prb.domain.ProbeAliasDomain;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeAlias;
import org.jax.mgi.mgd.api.util.Constants;

public class ProbeAliasTranslator extends BaseEntityDomainTranslator<ProbeAlias, ProbeAliasDomain> {

	@Override
	protected ProbeAliasDomain entityToDomain(ProbeAlias entity) {
		
		ProbeAliasDomain domain = new ProbeAliasDomain();
		
		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
		domain.setAliasKey(String.valueOf(entity.get_alias_key()));
		domain.setReferenceKey(String.valueOf(entity.get_reference_key()));
		domain.setAlias(entity.getAlias());
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
				
		return domain;
	}

}
