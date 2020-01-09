package org.jax.mgi.mgd.api.model.mgi.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.mgi.domain.MGISetMemberDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.MGISetMember;
import org.jax.mgi.mgd.api.util.Constants;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MGISetMemberTranslator extends BaseEntityDomainTranslator<MGISetMember, MGISetMemberDomain> {

	@Override
	protected MGISetMemberDomain entityToDomain(MGISetMember entity) {
		MGISetMemberDomain domain = new MGISetMemberDomain();
		
		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
		domain.setSetMemberKey(String.valueOf(entity.get_setmember_key()));
		domain.setSetKey(String.valueOf(entity.getMgiSet().get_set_key()));
		domain.setSetName(entity.getMgiSet().getName());
		domain.setObjectKey(String.valueOf(entity.get_object_key()));
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
		
		return domain;
	}

}
