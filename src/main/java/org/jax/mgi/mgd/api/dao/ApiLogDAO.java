package org.jax.mgi.mgd.api.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.jax.mgi.mgd.api.entities.ApiLogEvent;
import org.jax.mgi.mgd.api.entities.ApiLogObject;
import org.jax.mgi.mgd.api.entities.MGIType;
import org.jax.mgi.mgd.api.entities.User;
import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.util.Checks;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.DateParser;
import org.jax.mgi.mgd.api.util.SearchResults;

public class ApiLogDAO extends PostgresSQLDAO<ApiLogEvent> {

	protected ApiLogDAO() {
		super(ApiLogEvent.class);
	}

	/* Override the default search implementation, as we need to be able to search using fields
	 * from a 1-to-many relationship (List of ApiLogObjects).  Default implementation handles
	 * fields in main table and fields in 1-to-1 associations.
	 * 
	 * Expected fields:
	 *		_event_key (integer)
	 *		username (case insensitive string)
	 *		creation_date (with operators -- see DateParser class)
	 *		mgitype (case insensitive string)
	 *		_object_key (integer)
	 * Order of items returned is from newest to oldest (using key for faster sort than date).
	 */
	@Override
	public SearchResults<ApiLogEvent> search(Map<String, Object> params) {
		SearchResults<ApiLogEvent> results = new SearchResults<ApiLogEvent>();
		 
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<ApiLogEvent> query = builder.createQuery(myClass);
		Root<ApiLogEvent> root = query.from(myClass);
		query.orderBy(builder.desc(root.get("_event_key")));

		List<Predicate> restrictions = new ArrayList<Predicate>();
		
		// handle parameters right in the main table (mgi_apilog_event): _event_key, username, creation_date
		
		if (params.containsKey("_event_key")) {
			if (!Checks.isInteger(params.get("_event_key").toString())) {
				results.setError("InvalidInteger", "_event_key is not a valid integer", Constants.HTTP_BAD_REQUEST);
				return results;
			}
			Integer eventKey = Integer.parseInt(params.get("_event_key").toString());
			restrictions.add(builder.equal(root.get("_event_key"), eventKey));
		}
		
		if (params.containsKey("username")) {
			String username = (String) params.get("username");
			Join<ApiLogEvent,User> createdBy = root.join("createdBy");
			Path<String> login = createdBy.get("login");
			restrictions.add(builder.equal(builder.lower(login), username.toLowerCase()));
		}
		
		if (params.containsKey("creation_date")) {
			DateParser parser = new DateParser();
			try {
				// datePieces will either have [ start operator, start date ]
				// or [ start operator, start date, end operator, end date ]
				List<String> datePieces = parser.parse((String) params.get("creation_date"));
				if (datePieces.size() >= 2) {
					Path<Date> path = root.get("creation_date");
					restrictions.add(datePredicate(builder, path, datePieces.get(0), datePieces.get(1)));
				}
				if (datePieces.size() == 4) {
					Path<Date> path = root.get("creation_date");
					restrictions.add(datePredicate(builder, path, datePieces.get(2), datePieces.get(3)));
				}
			} catch (APIException e) {
				results.setError("InvalidDateFormat", "creation_date is invalid", Constants.HTTP_BAD_REQUEST);
				return results;
			}
		}

		// handle parameters from subtable (mgi_apilog_object) : mgitype, _object_key
		
		if (params.containsKey("mgitype") || params.containsKey("_object_key")) {
			Subquery<ApiLogObject> objSubquery = query.subquery(ApiLogObject.class);
			Root<ApiLogObject> objRoot = objSubquery.from(ApiLogObject.class);
			objSubquery.select(objRoot);
			
			List<Predicate> objPredicates = new ArrayList<Predicate>();
			objPredicates.add(builder.equal(root.get("_event_key"), objRoot.get("event").get("_event_key")));
			
			if (params.containsKey("_object_key")) {
				if (!Checks.isInteger(params.get("_object_key").toString())) {
					results.setError("InvalidInteger", "_object_key is not a valid integer", Constants.HTTP_BAD_REQUEST);
					return results;
				}
				Integer objectKey = Integer.parseInt(params.get("_object_key").toString());
				objPredicates.add(builder.equal(objRoot.get("_object_key"), objectKey));
			}
			
			if (params.containsKey("mgitype")) {
				String mgitype = (String) params.get("mgitype");
				Join<ApiLogObject,MGIType> mgiType = objRoot.join("mgiType");
				Path<String> name = mgiType.get("name");
				objPredicates.add(builder.equal(builder.lower(name), mgitype.toLowerCase())); 
			}
			
			objSubquery.where(objPredicates.toArray(new Predicate[]{}));
			restrictions.add(builder.exists(objSubquery));
		}

		query.where(builder.and(restrictions.toArray(new Predicate[0])));
		List<ApiLogEvent> events = entityManager.createQuery(query).setMaxResults(500).getResultList();
		log.info("got " + events.size() + " log events");
		results.setItems(events);

		return results;
	}
	
}
