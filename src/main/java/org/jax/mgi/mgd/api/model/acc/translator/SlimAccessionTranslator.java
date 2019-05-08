package org.jax.mgi.mgd.api.model.acc.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.acc.domain.SlimAccessionDomain;
import org.jax.mgi.mgd.api.model.acc.entities.Accession;
import org.jboss.logging.Logger;

public class SlimAccessionTranslator extends BaseEntityDomainTranslator<Accession, SlimAccessionDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Override
	protected SlimAccessionDomain entityToDomain(Accession entity) {
		SlimAccessionDomain domain = new SlimAccessionDomain();

		domain.setAccessionKey(String.valueOf(entity.get_accession_key()));
		domain.setLogicaldbKey(String.valueOf(entity.getLogicaldb().get_logicaldb_key()));
		domain.setObjectKey(String.valueOf(entity.get_object_key()));
		domain.setMgiTypeKey(String.valueOf(entity.getMgiType().get_mgitype_key()));
		domain.setAccID(entity.getAccID());
		domain.setPrefixPart(entity.getPrefixPart());
		domain.setNumericPart(String.valueOf(entity.getNumericPart()));	
		//domain.setIsPrivate(String.valueOf(entity.getIsPrivate()));
		//domain.setPreferred(String.valueOf(entity.getPreferred()));
		
		return domain;
	}

}
