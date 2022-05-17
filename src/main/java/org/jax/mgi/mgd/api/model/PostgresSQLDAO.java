package org.jax.mgi.mgd.api.model;

import java.lang.reflect.Field;
import java.util.Set;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jboss.logging.Logger;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;

@Singleton
public abstract class PostgresSQLDAO<T> {

	protected Class<T> myClass;

	@PersistenceContext(unitName="primary")
	protected EntityManager entityManager;

	protected Logger log = Logger.getLogger(getClass());
	protected String idFieldName = null;
	
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
}
