package org.jax.mgi.mgd.api.model.mgi.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.mgi.domain.MGIKeyValueDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.MGIKeyValue;
import org.jax.mgi.mgd.api.util.Constants;

public class MGIKeyValueTranslator extends BaseEntityDomainTranslator<MGIKeyValue, MGIKeyValueDomain> {

	@Override
	protected MGIKeyValueDomain entityToDomain(MGIKeyValue entity) {

		MGIKeyValueDomain domain = new MGIKeyValueDomain();
		
		domain.set_keyvalue_key(String.valueOf(entity.get_keyvalue_key()));
		domain.set_object_key(String.valueOf(entity.get_object_key()));
		domain.set_mgitype_key(String.valueOf(entity.get_mgitype_key()));
		domain.setSequenceNum(String.valueOf(entity.getSequenceNum()));
		domain.setKey(entity.getKey());
		domain.setValue(entity.getValue());
	
		return domain;
	}

}
