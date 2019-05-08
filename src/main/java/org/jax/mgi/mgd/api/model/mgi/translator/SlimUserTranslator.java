package org.jax.mgi.mgd.api.model.mgi.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.mgi.domain.SlimUserDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.User;

public class SlimUserTranslator extends BaseEntityDomainTranslator<User, SlimUserDomain> {

	@Override
	protected SlimUserDomain entityToDomain(User entity) {
		SlimUserDomain domain = new SlimUserDomain();
		
		domain.setUserKey(String.valueOf(entity.get_user_key()));
		domain.setUserLogin(entity.getLogin());
		
		return domain;
	}

}
