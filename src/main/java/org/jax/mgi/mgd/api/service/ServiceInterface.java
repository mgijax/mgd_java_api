package org.jax.mgi.mgd.api.service;

import java.util.Map;

import org.jax.mgi.mgd.api.model.PostgresSQLDAO;
import org.jax.mgi.mgd.api.util.SearchResults;

import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class ServiceInterface<T> {
	
	protected ObjectMapper mapper = new ObjectMapper();
	
	public T create(T object) {
		return getDAO().create(object);
	}

	public T update(T object) {
		return getDAO().update(object);
	}

	public T get(Integer key) {
		return getDAO().get(key);
	}
	
	public T delete(Integer key) {
		return getDAO().delete(getDAO().get(key));
	}

	public SearchResults<T> search(Map<String, Object> searchFields) {
		return getDAO().search(searchFields);
	}
	
	public SearchResults<T> search(Map<String, Object> searchFields, String orderByField) {
		return getDAO().search(searchFields, orderByField);
	}
	
	public abstract PostgresSQLDAO<T> getDAO();
}
