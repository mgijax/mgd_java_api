package org.jax.mgi.mgd.api.model.mgi.repository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.model.Repository;
import org.jax.mgi.mgd.api.model.acc.dao.MGITypeDAO;
import org.jax.mgi.mgd.api.model.acc.entities.MGIType;
import org.jax.mgi.mgd.api.model.mgi.dao.ApiLogDAO;
import org.jax.mgi.mgd.api.model.mgi.domain.ApiLogDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.ApiLogEvent;
import org.jax.mgi.mgd.api.model.mgi.entities.ApiLogObject;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.translators.ApiLogTranslator;
import org.jax.mgi.mgd.api.util.MapMaker;
import org.jax.mgi.mgd.api.util.SearchResults;

/* Is: a repository that deals with ApiLogDomain objects and handles their translation to
 *    ApiLogEvent entity objects for storage to and retrieval from the database
 * Has: one or more DAOs to facilitate storage/retrieval of the entities from which the
 *    ApiLogDomain object has its data drawn
 * Does: (from the outside, this appears to) retrieve domain objects, store them, search for them
 */
public class ApiLogRepository extends Repository<ApiLogDomain> {

	/***--- instance variables ---***/

	private SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");

	@Inject
	private ApiLogDAO apiLogDAO;

	@Inject
	private MGITypeDAO mgitypeDAO;

	ApiLogTranslator translator = new ApiLogTranslator();
	MapMaker mapMaker = new MapMaker();

	/***--- (public) instance methods ---***/

	/* gets an ApiLogDomain object that is fully fleshed out from an ApiLogEvent specified by key
	 */
	@Override
	public ApiLogDomain get(int primaryKey) throws APIException {
		ApiLogEvent event = getApiLogEvent(primaryKey);
		ApiLogDomain domain = translator.translate(event);
		return domain;
	}

	@Override
	public SearchResults<ApiLogDomain> search(Map<String,Object> params) {
		SearchResults<ApiLogEvent> events = apiLogDAO.search(params);
		SearchResults<ApiLogDomain> domains = new SearchResults<ApiLogDomain>();

		domains.elapsed_ms = events.elapsed_ms;
		domains.error = events.error;
		domains.message = events.message;
		domains.status_code = events.status_code;
		domains.total_count = events.total_count;

		if (events.items != null) {
			// walking the log entries to do the translations individually, because I want a List,
			// not an Iterable

			domains.items = new ArrayList<ApiLogDomain>();
			for (ApiLogEvent event : events.items) {
				domains.items.add(translator.translate(event));
			}
		}
		return domains;
	}

	@Override
	public ApiLogDomain update(ApiLogDomain domain, User user) throws APIException {
		ApiLogEvent entity = getApiLogEvent(domain._event_key);
		applyDomainChanges(entity, domain, user);
		return translator.translate(entity);
	}

	@Override
	public ApiLogDomain delete(ApiLogDomain domain, User user) throws APIException {
		throw new APIException("Need to implement ApiLogRepository.delete() method");
	}

	@Override
	public ApiLogDomain create(ApiLogDomain domain, User username) throws APIException {
		ApiLogEvent entity = new ApiLogEvent();
		applyDomainChanges(entity, domain, username);
		return translator.translate(entity);
	}

	/***--- (private) instance methods ---***/

	/* retrieve the ApiLogEvent object with the given primaryKey
	 */
	private ApiLogEvent getApiLogEvent(Integer primaryKey) throws APIException {
		if (primaryKey == null) {
			throw new APIException("ApiLogRepository.getApiLogEvent() : primary key is null");
		}
		ApiLogEvent entity = apiLogDAO.get(primaryKey);
		if (entity == null) {
			throw new APIException("ApiLogRepository.getApiLogEvent(): Unknown event key: " + primaryKey);
		}
		return entity;
	}

	/* take the data from the domain object and overwrite any changed data into the entity object
	 * (assumes we are working in a transaction -- persists any sub-objects into the database, after
	 * persisting this entity object itself)
	 */
	private void applyDomainChanges(ApiLogEvent entity, ApiLogDomain domain, User user) throws APIException {
		// Note that we must have 'anyChanges' after the OR, otherwise short-circuit evaluation will only save
		// the first section changed.

		if (domain._event_key == null) {
			domain._event_key = apiLogDAO.getNextKey("ApiLogEvent", "_event_key");
			entity.set_event_key(domain._event_key);
		}
		try {
			entity.setCreation_date(dateFormatter.parse(domain.creation_date));
		} catch (Exception e) {
			throw new APIException("Cannot parse date (" + domain.creation_date + "), " + e.toString());
		}
		entity.setEndpoint(domain.endpoint);
		entity.setParameters(domain.parameters);
		entity.setCreatedBy(user);

		// must save the main log entry first, then can do the associated objects
		apiLogDAO.persist(entity);
		applyObjectChanges(entity, domain);
	}

	/* apply any changes in associated objects from domain to entity
	 */
	private void applyObjectChanges(ApiLogEvent entity, ApiLogDomain domain) throws APIException {
		Set<ApiLogObject> entityObjects = entity.getObjects();
		List<Integer> domainObjectKeys = domain.objectKeys;

		if (entityObjects == null) { entityObjects = new HashSet<ApiLogObject>(); }
		if (domainObjectKeys == null) { domainObjectKeys = new ArrayList<Integer>(); }

		Set<Integer> toAdd = new HashSet<Integer>();
		for (Integer objectKey : domainObjectKeys) {
			toAdd.add(objectKey);
		}

		List<ApiLogObject> toDelete = new ArrayList<ApiLogObject>();
		for (ApiLogObject obj : entityObjects) {
			if (toAdd.contains(obj.get_object_key())) {
				// still in list; don't need to add it
				toAdd.remove(obj.get_object_key());
			} else {
				// no longer in list, so will remove from db
				toDelete.add(obj);
			}
		}

		// remove from the database those objects that are no longer associated with the event
		for (ApiLogObject obj : toDelete) {
			apiLogDAO.remove(obj);
		}

		MGIType mgiType = getMgitype(domain.mgitype);

		// add any new objects that need to be associated with the event
		for (Integer objectKey : toAdd) {
			ApiLogObject loggedObject = new ApiLogObject();
			loggedObject.set_LogObject_key(apiLogDAO.getNextKey("ApiLogObject", "_LogObject_key"));
			loggedObject.setEvent(entity);
			loggedObject.setMgiType(mgiType);
			loggedObject.set_object_key(objectKey);
			apiLogDAO.persist(loggedObject);
		}
	}


	/* return a single MGIType matching the given name
	 */
	private MGIType getMgitype (String name) throws APIException {
		MapMaker mapMaker = new MapMaker();
		try {
			SearchResults<MGIType> types = mgitypeDAO.search(mapMaker.toMap("{\"name\" : \"" + name + "\"}"));

			if ((types.items == null) || (types.items.size() == 0)) {
				throw new APIException("ApiLogRepository: Could not find MGIType for name: " + name);
			} else if (types.items.size() > 1) {
				throw new APIException("ApiLogRepository: Found too many MGITypes for name: " + name);
			}

			return types.items.get(0);
		} catch (Exception e) {
			throw new APIException("ApiLogRepository: MGIType search failed: " + e.toString());
		}
	}
}
