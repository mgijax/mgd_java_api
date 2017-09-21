package org.jax.mgi.mgd.api.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.jax.mgi.mgd.api.entities.ApiTableLog;
import org.jax.mgi.mgd.api.entities.Term;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Singleton
public abstract class PostgresSQLDAO<T> {
	/***--- instance variables ---***/

	private ObjectMapper mapper = new ObjectMapper();
	
	protected Class<T> myClass;

	@PersistenceContext(unitName="primary")
	protected EntityManager entityManager;

	protected Logger log = Logger.getLogger(PostgresSQLDAO.class);

	/* how long can we cache keys without refreshing from database? (in milliseconds) */
	protected static long expirationTime = 2 * 60 * 1000;

	/* maps from table name to time (in milliseconds) when we should search for a new key */
	protected static Map<String, Long> keyExpiration = new HashMap<String, Long>();

	/* maps from table name to the next value that should be assigned as primary key for the table */
	protected static Map<String, Integer> nextKeyValue = new HashMap<String, Integer>();

	/***--- methods ---***/

	protected PostgresSQLDAO(Class<T> myClass) {
		this.myClass = myClass;

		//myClass = (Class<T>) DAOUtil.getTypeArguments(Foo.class, this.getClass()).get(0);
		//myClass = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	public T find(int primaryKey) {
		T myT = entityManager.find(this.myClass, primaryKey);
		return myT;
	}
	public T create(T model) {
		//log(model);
		entityManager.persist(model);
		return model;
	}

	private void log(T model) {
		Integer id = (Integer)entityManager.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(model);
		try {
			ApiTableLog log = new ApiTableLog(id, model.getClass().toString(), mapper.writeValueAsString(get(id)), mapper.writeValueAsString(model));
			entityManager.persist(log);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	public T update(T model) {
		//log(model);
		entityManager.merge(model);
		return model;
	}

	public T get(HashMap<String, Object> params) {
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
		//log(model);
		entityManager.remove(model);
		return model;
	}

	/* default query handling; good for fields directly in the table backing model class T
	 */
	public SearchResults<T> search(Map<String, Object> params) {
		return search(params, null);
	}
	
	public SearchResults<T> search(Map<String, Object> params, String orderByField) {
		log.info("Lookup: " + params);
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


	/* get the next available _Accession_key in the ACC_Accession table
	 */
	public synchronized int getNextAccessionKey() {
		return this.getNextKey("AccessionID", "_accession_key");
	}

	/* method to get the next available key for the specified 'fieldName' in the given 'tableName'.  Any methods
	 * that wrap this method should be synchronized to ensure thread-safety.  (We do not synchronize this method
	 * itself, as we want key requests for different tables to be able to proceed in parallel.)
	 */
	public int getNextKey(String tableName, String fieldName) {
		Long currentTime = System.currentTimeMillis();

		/* To save hitting the database for every request (and to avoid the same key being given to two users
		 * who are curating at the same time), we cache the latest key assigned for a given table for a certain
		 * period of time.  If we pass that period of time without requesting another key for that table, then
		 * we will query the database when the next key is requested.  (This lets loads handle their own key
		 * assignments overnight and on weekends, without getting out of sync with an in-memory cached value here.)
		 */

		if (!keyExpiration.containsKey(tableName) || (currentTime > keyExpiration.get(tableName))) {
			TypedQuery<Integer> q1 = (TypedQuery<Integer>) entityManager.createQuery("select max(" + fieldName + ") from " + tableName, Integer.class);
			Integer maxKey = q1.getSingleResult();
			if (maxKey == null) {
				maxKey = 0;
			}
			nextKeyValue.put(tableName, ++maxKey);
		}
		Integer nextKey = nextKeyValue.get(tableName);
		nextKeyValue.put(tableName, nextKey + 1);
		keyExpiration.put(tableName, currentTime + expirationTime);
		return nextKey;
	}

	/* Sometimes we need to persist a sub-object before it can be added to a collection in a parent object;
	 * this method facilitates that initial save of the sub-object.
	 */
	public void persist(Object o) {
		this.entityManager.persist(o);
	}

	/* Remove object o from the database.  Make sure it has already been removed from any relationships.
	 */
	public void remove(Object o) {
		this.entityManager.remove(o);
	}

	/* Refresh object o from the database.
	 */
	public void refresh(Object o) {
		this.entityManager.refresh(o);
	}

	/* method to get the database dump date from the mgi_dbinfo table
	 */
	public String getDumpDate() {
		TypedQuery<String> q1 = (TypedQuery<String>) entityManager.createQuery("select lastdump_date from DatabaseInfo", String.class);
		String dumpDate = q1.getSingleResult();
		if (dumpDate == null) {
			dumpDate = "unknown";
		}
		return dumpDate;
	}

}
