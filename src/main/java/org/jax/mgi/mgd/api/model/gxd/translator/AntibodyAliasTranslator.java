package org.jax.mgi.mgd.api.model.gxd.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.gxd.domain.AntibodyAliasDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.AntibodyAlias;
import org.jboss.logging.Logger;

public class AntibodyAliasTranslator extends BaseEntityDomainTranslator<AntibodyAlias, AntibodyAliasDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Override
	protected AntibodyAliasDomain entityToDomain(AntibodyAlias entity) {

		AntibodyAliasDomain domain = new AntibodyAliasDomain();

		domain.setAntibodyAliasKey(String.valueOf(entity.get_antibodyalias_key()));
		domain.setAntibodyKey(String.valueOf(entity.get_antibody_key()));
		domain.setAlias(entity.getAlias());
		domain.setRefsKey(String.valueOf(entity.getReference().get_refs_key()));
		domain.setJnumid(entity.getReference().getReferenceCitationCache().getJnumid());
		domain.setJnum(String.valueOf(entity.getReference().getReferenceCitationCache().getNumericPart()));
		domain.setShort_citation(entity.getReference().getReferenceCitationCache().getShort_citation());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
		
		return domain;
	}

}
