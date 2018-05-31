package org.jax.mgi.mgd.api.model.mgi.translator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.mgi.domain.ApiLogDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.ApiLogEvent;
import org.jax.mgi.mgd.api.model.mgi.entities.ApiLogObject;

public class ApiLogTranslator extends BaseEntityDomainTranslator<ApiLogEvent, ApiLogDomain>{
	private SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
	
	@Override
	protected ApiLogDomain entityToDomain(ApiLogEvent entity, int translationDepth) {
		ApiLogDomain domain = new ApiLogDomain();
		domain._event_key = entity.get_event_key();
		domain.creation_date = dateFormatter.format(entity.getCreation_date());
		domain.endpoint = entity.getEndpoint();
		domain.parameters = entity.getParameters();
		domain.objectKeys = new ArrayList<Integer>();
		domain.mgitype = null;
		domain.username = entity.getCreatedBy().getLogin();
		
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
	protected ApiLogEvent domainToEntity(ApiLogDomain domain, int translationDepth) {
		// Cannot do translation here, as it requires lookup of actual entity from database.  Must
		// instead work with ApiLogRepository.
		
		return null;
	}
}
