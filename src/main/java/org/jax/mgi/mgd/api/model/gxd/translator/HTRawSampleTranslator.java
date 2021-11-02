package org.jax.mgi.mgd.api.model.gxd.translator;

import java.util.List;
import java.util.ArrayList;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.gxd.domain.HTRawSampleDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.HTRawSampleVariableDomain;
import org.jax.mgi.mgd.api.model.voc.entities.Term;
import org.jax.mgi.mgd.api.model.mgi.entities.MGIKeyValue;
import org.jax.mgi.mgd.api.model.gxd.entities.HTRawSample;

import org.jboss.logging.Logger;

public class HTRawSampleTranslator extends BaseEntityDomainTranslator<HTRawSample, HTRawSampleDomain> {
	
	@Override
	protected HTRawSampleDomain entityToDomain(HTRawSample entity) {

		Logger log = Logger.getLogger(getClass());
		
		HTRawSampleDomain sampleDomain = new HTRawSampleDomain();

		// Sample Info
		sampleDomain.set_rawsample_key(entity.get_rawsample_key());
		sampleDomain.set_experiment_key(entity.get_experiment_key());
		sampleDomain.setAccid(entity.getAccid());

		// key value pairs 
		if (entity.getKeyValuePairs() != null) {
			List<HTRawSampleVariableDomain> variableDomains = new ArrayList<HTRawSampleVariableDomain>();
			for (MGIKeyValue keyValue : entity.getKeyValuePairs()) {
				HTRawSampleVariableDomain variableDomain = new HTRawSampleVariableDomain();
				variableDomain.setName(keyValue.getKey());
				variableDomain.setValue(keyValue.getValue());
				variableDomains.add(variableDomain);
			}
			sampleDomain.setVariable(variableDomains);
		}


		return sampleDomain;
	}

}

