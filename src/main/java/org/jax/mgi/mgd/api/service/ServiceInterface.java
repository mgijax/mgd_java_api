package org.jax.mgi.mgd.api.service;

import java.util.Map;

import org.jax.mgi.mgd.api.domain.DomainBase;
import org.jax.mgi.mgd.api.util.SearchResults;

import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class ServiceInterface<D extends DomainBase> {
	
	protected ObjectMapper mapper = new ObjectMapper();
	
	public abstract D create(D object);
	public abstract D update(D object);
	public abstract D get(Integer key);
	public abstract D delete(Integer key);
	public abstract SearchResults<D> search(Map<String, Object> searchFields);
	public abstract SearchResults<D> search(Map<String, Object> searchFields, String orderByField);

}
