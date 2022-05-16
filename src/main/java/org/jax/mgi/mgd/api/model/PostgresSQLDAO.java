package org.jax.mgi.mgd.api.model;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;

@Singleton
public abstract class PostgresSQLDAO<T> {

	protected Class<T> myClass;

	@PersistenceContext(unitName="primary")
	protected EntityManager entityManager;

	protected Logger log = Logger.getLogger(getClass());

	// ONLY USED BY bib/LT (LitTriage)
	
	/* how long can we cache keys without refreshing from database? (in milliseconds) */
	protected static long expirationTime = 2 * 60 * 1000;

	/* maps from table name to time (in milliseconds) when we should search for a new key */
	protected static Map<String, Long> keyExpiration = new HashMap<String, Long>();

	/* maps from table name to the next value that should be assigned as primary key for the table */
	protected static Map<String, Integer> nextKeyValue = new HashMap<String, Integer>();
	
	protected String idFieldName = null;

	// END ONLY USED BY bib/LT (LitTriage)
	
	protected PostgresSQLDAO(Class<T> myClass) {
		this.myClass = myClass;
		Reflections r = new Reflections(myClass.getPackage().getName(), new FieldAnnotationsScanner());
		
		Set<Field> fields = r.getFieldsAnnotatedWith(Id.class);

		for(Field f: fields) {
			if(f.getDeclaringClass().getName().equals(myClass.getName())) {
				idFieldName = f.getName();
				break;
			}
		}
	}

	public T find(int primaryKey) {
		T myT = entityManager.find(this.myClass, primaryKey);
		return myT;
	}
	public T create(T model) {
		entityManager.persist(model);
		return model;
	}

	public T update(T model) {
		entityManager.merge(model);
		return model;
	}

	public T get(Map<String, Object> params) {
		return search(params).items.get(0);
	}

	public T get(Integer key) {
		return entityManager.find(myClass, key);
	}

	public T get(T model) {
		entityManager.merge(model);
		return model;
	}

	public T delete(T model) {
		entityManager.remove(model);
		return model;
	}

	/* Persist object o to the database
	 * Sometimes we need to persist a sub-object before it can be added to a collection in a parent object;
	 * this method facilitates that initial save of the sub-object.
	 */
	public void persist(Object o) {
		entityManager.persist(o);
	}

	/* Remove object o from the database.  Make sure it has already been removed from any relationships.
	 */
	public void remove(Object o) {
		entityManager.remove(o);
	}

	/* Refresh object o from the database.
	 */
	public void refresh(Object o) {
		entityManager.refresh(o);
	}

	/* Remove all entities from the persistence context
	 */
	public void clear() {
		entityManager.clear();
	}
	
	/* 
	 * used to call a postgres stored procedure
	 */
	public Query createNativeQuery(String cmd) {
		Query query = entityManager.createNativeQuery(cmd);
		return query;
	}

	//
	// ONLY USED BY bib/LT (LitTriage)
	//

	/* default query handling; good for fields directly in the table backing model class T
	 */
	public SearchResults<T> search(Map<String, Object> params) {
		return search(params, null);
	}

	public SearchResults<T> search(Map<String, Object> params, String orderByField) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<T> query = builder.createQuery(myClass);
		Root<T> root = query.from(myClass);

		List<Predicate> restrictions = new ArrayList<Predicate>();

		for(String key: params.keySet()) {
			Path<Object> column = null;
			if(key.contains(".")) {
				String[] objects = key.split("\\.");
				for(String s: objects) {
					if(column != null) {
						column = column.get(s);
					} else {
						column = root.get(s);
					}
				}
			} else {
				column = root.get(key);
			}

			Object value = params.get(key);
			if (value instanceof Integer) {
				Integer desiredValue = (Integer) value;
				restrictions.add(builder.equal(column, desiredValue));
			} else {
				String desiredValue = (String) value;
				restrictions.add(builder.equal(column, desiredValue));
			}
		}
		if(orderByField != null) {
			query.orderBy(builder.asc(root.get(orderByField)));
		}

		query.where(builder.and(restrictions.toArray(new Predicate[0])));

		SearchResults<T> results = new SearchResults<T>();
		results.setItems(entityManager.createQuery(query).getResultList());
		return results;
	}

	/* method to get the next available key for the specified 'fieldName' in the given 'tableName'.  Any methods
	 * that wrap this method should be synchronized to ensure thread-safety.  (We do not synchronize this method
	 * itself, as we want key requests for different tables to be able to proceed in parallel.)
	 */
	public int getNextKey() {
		Long currentTime = System.currentTimeMillis();

		if(idFieldName == null) {
			log.error("idField was not found or multiple fields exist please use another method for getting max key");
			return 0;
		}
		/* To save hitting the database for every request (and to avoid the same key being given to two users
		 * who are curating at the same time), we cache the latest key assigned for a given table for a certain
		 * period of time.  If we pass that period of time without requesting another key for that table, then
		 * we will query the database when the next key is requested.  (This lets loads handle their own key
		 * assignments overnight and on weekends, without getting out of sync with an in-memory cached value here.)
		 */

		if (!keyExpiration.containsKey(idFieldName) || (currentTime > keyExpiration.get(idFieldName))) {
			TypedQuery<Integer> q1 = entityManager.createQuery("select max(" + idFieldName + ") from " + myClass.getSimpleName(), Integer.class);
			Integer maxKey = q1.getSingleResult();
			if (maxKey == null) {
				maxKey = 0;
			}
			nextKeyValue.put(idFieldName, ++maxKey);
		}
		Integer nextKey = nextKeyValue.get(idFieldName);
		nextKeyValue.put(idFieldName, nextKey + 1);
		keyExpiration.put(idFieldName, currentTime + expirationTime);
		return nextKey;
	}

}
