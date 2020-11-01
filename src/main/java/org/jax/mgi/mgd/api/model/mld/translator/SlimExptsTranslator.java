package org.jax.mgi.mgd.api.model.mld.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.mld.domain.SlimExptsDomain;
import org.jax.mgi.mgd.api.model.mld.entities.Expts;
import org.jboss.logging.Logger;

public class SlimExptsTranslator extends BaseEntityDomainTranslator<Expts, SlimExptsDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Override
	protected SlimExptsDomain entityToDomain(Expts entity) {
		
		SlimExptsDomain domain = new SlimExptsDomain();

		domain.setExptKey(String.valueOf(entity.get_expt_key()));
		domain.setExptDisplay(entity.getReference().getReferenceCitationCache().getJnumid() + ", " + entity.getExptType() + ", Chr " + entity.getChromosome());
		domain.setExptType(entity.getExptType());

		return domain;
	}

}
