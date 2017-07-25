package org.jax.mgi.mgd.api.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//import javax.annotation.Resource;
//import javax.ejb.SessionContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.jboss.logging.Logger;


public class PostgresSQLDAO<T> {

	protected Class<T> myClass;

//	@Resource
//	protected SessionContext sessionContext;
	
	@PersistenceContext(unitName="primary")
	protected EntityManager entityManager;
	
	protected Logger log = Logger.getLogger(PostgresSQLDAO.class);

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
	
}
