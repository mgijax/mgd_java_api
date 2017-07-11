package org.jax.mgi.mgd.api.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.jax.mgi.mgd.api.entities.Base;
import org.jboss.logging.Logger;


public class PostgresSQLDAO<T extends Base> {

	protected Class<T> myClass;

	@PersistenceContext(unitName="primary")
	private EntityManager entityManager;
	
	private Logger log = Logger.getLogger(PostgresSQLDAO.class);

	protected void setClazz(Class<T> myClass){
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

	public List<T> get(HashMap<String, Object> params) {
		log.info("Lookup: " + params);
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<T> query = builder.createQuery(myClass);
		Root<T> root = query.from(myClass);

		List<Predicate> restrictions = new ArrayList<Predicate>();
		
		for(String key: params.keySet()) {
			Object desiredValue = params.get(key);
			if ((desiredValue instanceof String) && (((String) desiredValue).indexOf("%") >= 0)) {
				// has at least one wildcard, so do 'like' search
				restrictions.add(builder.like(root.get(key), (String) desiredValue));
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
