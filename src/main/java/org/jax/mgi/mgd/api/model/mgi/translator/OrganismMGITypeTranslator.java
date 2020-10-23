package org.jax.mgi.mgd.api.model.mgi.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.mgi.domain.OrganismMGITypeDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.OrganismMGIType;
import org.jax.mgi.mgd.api.util.Constants;

public class OrganismMGITypeTranslator extends BaseEntityDomainTranslator<OrganismMGIType, OrganismMGITypeDomain> {

	@Override
	protected OrganismMGITypeDomain entityToDomain(OrganismMGIType entity) {
		OrganismMGITypeDomain domain = new OrganismMGITypeDomain();

		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);		
		domain.setAssocKey(String.valueOf(entity.get_assoc_key()));
		domain.setOrganismKey(String.valueOf(entity.get_organism_key()));
		domain.setMgiTypeKey(String.valueOf(entity.getMgiType().get_mgitype_key()));
		domain.setMgiType(entity.getMgiType().getName());
		domain.setSequenceNum(entity.getSequenceNum());
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
		
		return domain;
	}

}
