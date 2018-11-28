package org.jax.mgi.mgd.api.model.acc.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.acc.domain.AccessionDomain;
import org.jax.mgi.mgd.api.model.acc.entities.Accession;

public class AccessionTranslator extends BaseEntityDomainTranslator<Accession, AccessionDomain> {
	
	@Override
	protected AccessionDomain entityToDomain(Accession entity, int translationDepth) {
		AccessionDomain domain = new AccessionDomain();

		domain.set_accession_key(entity.get_accession_key());
		//domain.set_logicaldb_key(entity.getLogicaldb().get_logicaldb_key());
		//domain.set_object_key(entity.get_object_key());
		//domain.set_mgitype_key(entity.getMgiType().get_mgitype_key());
		
		// string versions
		domain.setAccessionKey(String.valueOf(entity.get_accession_key()));
		domain.setLogicaldbKey(String.valueOf(entity.getLogicaldb().get_logicaldb_key()));
		domain.setObjectKey(String.valueOf(entity.get_object_key()));
		domain.setMgiTypeKey(String.valueOf(entity.getMgiType().get_mgitype_key()));
		
		domain.setAccID(entity.getAccID());
		domain.setPrefixPart(entity.getPrefixPart());
		domain.setNumericPart(entity.getNumericPart());	
		domain.setIs_private(entity.getIs_private());
		domain.setPreferred(entity.getPreferred());
		
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setCreatedBy(entity.getCreatedBy().getName());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setModifiedBy(entity.getModifiedBy().getName());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));

		return domain;
	}

	@Override
	protected Accession domainToEntity(AccessionDomain domain, int translationDepth) {
		// Needs to be implemented once we choose to save terms
		return null;
	}

}
