package org.jax.mgi.mgd.api.translators;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.jax.mgi.mgd.api.domain.ApiLogDomain;
import org.jax.mgi.mgd.api.entities.ApiLogEvent;
import org.jax.mgi.mgd.api.entities.ApiLogObject;

public class ApiLogTranslator extends EntityDomainTranslator<ApiLogEvent, ApiLogDomain>{
	private SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
	
	@Override
	protected ApiLogDomain entityToDomain(ApiLogEvent entity) {
		ApiLogDomain domain = new ApiLogDomain();
		domain._event_key = entity.get_event_key();
		domain.creation_date = dateFormatter.format(entity.getCreation_date());
		domain.endpoint = entity.getEndpoint();
		domain.parameters = entity.getParameters();
		domain.objectKeys = new ArrayList<Integer>();
		domain.mgitype = null;
		
		if (entity.getObjects() != null) {
			for (ApiLogObject object : entity.getObjects()) {
				domain.objectKeys.add(object.get_object_key());
				if (domain.mgitype == null) {
					domain.mgitype = object.getMgiType().getName();
				}
			}
		}
		return domain;
	}

	@Override
	protected ApiLogEvent domainToEntity(ApiLogDomain domain) {
		// Cannot do translation here, as it requires lookup of actual entity from database.  Must
		// instead work with ApiLogRepository.
		
		return null;
	}
}
