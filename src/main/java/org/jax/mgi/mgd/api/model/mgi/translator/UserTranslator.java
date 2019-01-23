package org.jax.mgi.mgd.api.model.mgi.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.mgi.domain.UserDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.Constants;

public class UserTranslator extends BaseEntityDomainTranslator<User, UserDomain> {

	@Override
	protected UserDomain entityToDomain(User entity, int translationDepth) {
		UserDomain domain = new UserDomain();
		
		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
		domain.setUserKey(String.valueOf(entity.get_user_key()));
		domain.setUserTypeKey(String.valueOf(entity.getUserType().get_term_key()));
		domain.setUserStatusKey(String.valueOf(entity.getUserStatus().get_term_key()));
		domain.setUserLogin(entity.getLogin());
		domain.setUserName(entity.getName());
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
		
		if (entity.getOrcid() != null) {
			domain.setOrcid(entity.getOrcid());			
		}
		 
		if (entity.getGroup() != null) {
			domain.setGroupKey(String.valueOf(entity.getGroup().get_term_key()));			
		}
		
		return domain;
	}

	@Override
	protected User domainToEntity(UserDomain domain, int translationDepth) {
		// TODO Auto-generated method stub
		return null;
	}

}
