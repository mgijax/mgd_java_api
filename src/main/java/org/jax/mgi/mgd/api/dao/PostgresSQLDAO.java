package org.jax.mgi.mgd.api.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.TypedQuery;

import org.jax.mgi.mgd.api.entities.Term;
import org.jax.mgi.mgd.api.entities.User;
import org.jboss.logging.Logger;


@Singleton
public class PostgresSQLDAO<T> {
	/***--- instance variables ---***/

	protected Class<T> myClass;

	@PersistenceContext(unitName="primary")
	protected EntityManager entityManager;
	
	protected Logger log = Logger.getLogger(PostgresSQLDAO.class);

	/* how long can we cache keys without refreshing from database? (in milliseconds) */
	private static long expirationTime = 2 * 60 * 1000;
	
	/* maps from table name to time (in milliseconds) when we should search for a new key */
	private static Map<String, Long> keyExpiration = new HashMap<String, Long>();
	
	/* maps from table name to the next value that should be assigned as primary key for the table */
	private static Map<String, Long> nextKeyValue = new HashMap<String, Long>();
	
	/***--- methods ---***/
	
	protected void setClass(Class<T> myClass){
		this.myClass = myClass;
	}

	public T add(T model) {
		entityManager.persist(model);
		return model;
	}

	public T update(T model) {
		entityManager.merge(model);
		return model;
	}

	/* default query handling; good for fields directly in the table backing model class T
	 */
	public List<T> get(HashMap<String, Object> params) {
		log.info("Lookup: " + params);
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<T> query = builder.createQuery(myClass);
		Root<T> root = query.from(myClass);

		List<Predicate> restrictions = new ArrayList<Predicate>();
		
		for(String key: params.keySet()) {
			Object desiredValue = params.get(key);
			if ((desiredValue instanceof String) && (((String) desiredValue).indexOf("%") >= 0)) {
				// has at least one wildcard, so do case-insensitive 'like' search
				restrictions.add(builder.like(builder.lower(root.get(key)), ((String) desiredValue).toLowerCase()));
			} else {
				// no wildcards, so do 'equals' search
				restrictions.add(builder.equal(root.get(key), params.get(key)));
			}
		}

		query.where(builder.and(restrictions.toArray(new Predicate[0])));

		return entityManager.createQuery(query).getResultList();
	}

	public T delete(T model) {
		entityManager.remove(model);
		return model;
	}
	
	/* get a User object for the given login
	 */
	public User getUser(String login) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<User> query = builder.createQuery(User.class);
		Root<User> root = query.from(User.class);
		List<Predicate> restrictions = new ArrayList<Predicate>();
		query.where(builder.equal(root.get("login"), login));
		List<User> results = entityManager.createQuery(query).getResultList();
		if ((results == null) || (results.size() == 0)) {
			return null;
		}
		return results.get(0);
	}

	/* get a Term object for the given vocabulary key and the term's abbreviation
	 */
	public Term getTermByAbbreviation(Integer vocabKey, String abbreviation) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Term> query = builder.createQuery(Term.class);
		Root<Term> root = query.from(Term.class);
		List<Predicate> restrictions = new ArrayList<Predicate>();
		restrictions.add(builder.equal(root.get("_vocab_key"), vocabKey));
		restrictions.add(builder.equal(root.get("abbreviation"), abbreviation));
		query.where(builder.and(restrictions.toArray(new Predicate[0])));
		List<Term> results = entityManager.createQuery(query).getResultList();
		if ((results == null) || (results.size() == 0)) {
			return null;
		}
		return results.get(0);
	}

	/* get a Term object for the given vocabulary key and the term's text
	 */
	public Term getTermByTerm(Integer vocabKey, String term) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Term> query = builder.createQuery(Term.class);
		Root<Term> root = query.from(Term.class);
		List<Predicate> restrictions = new ArrayList<Predicate>();
		restrictions.add(builder.equal(root.get("_vocab_key"), vocabKey));
		restrictions.add(builder.equal(root.get("term"), term));
		query.where(builder.and(restrictions.toArray(new Predicate[0])));
		List<Term> results = entityManager.createQuery(query).getResultList();
		if ((results == null) || (results.size() == 0)) {
			return null;
		}
		return results.get(0);
	}

	/* method to get the next available key for the specified 'fieldName' in the given 'tableName'.  Any methods
	 * that wrap this method should be synchronized to ensure thread-safety.  (We do not synchronize this method
	 * itself, as we want key requests for different tables to be able to proceed in parallel.)
	 */
	public long getNextKey(String tableName, String fieldName) {
		Long currentTime = System.currentTimeMillis();
		
		/* To save hitting the database for every request (and to avoid the same key being given to two users
		 * who are curating at the same time), we cache the latest key assigned for a given table for a certain
		 * period of time.  If we pass that period of time without requesting another key for that table, then
		 * we will query the database when the next key is requested.  (This lets loads handle their own key
		 * assignments overnight and on weekends, without getting out of sync with an in-memory cached value here.)
		 */
		
		if (!keyExpiration.containsKey(tableName) || (currentTime > keyExpiration.get(tableName))) {
			TypedQuery<Long> q1 = (TypedQuery<Long>) entityManager.createQuery("select max(" + fieldName + ") from " + tableName, Long.class);
			Long maxKey = q1.getSingleResult();
			if (maxKey == null) {
				maxKey = 0L;
			}
			nextKeyValue.put(tableName, ++maxKey);
		}
		Long nextKey = nextKeyValue.get(tableName);
		nextKeyValue.put(tableName, nextKey + 1);
		keyExpiration.put(tableName, currentTime + expirationTime);
		return nextKey;
	}
}