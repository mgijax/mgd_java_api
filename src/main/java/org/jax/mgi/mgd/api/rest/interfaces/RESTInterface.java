package org.jax.mgi.mgd.api.rest.interfaces;

import java.util.Map;

import org.jax.mgi.mgd.api.domain.DomainBase;
import org.jax.mgi.mgd.api.util.SearchResults;

public interface RESTInterface<T extends DomainBase> {
	public T create(String api_access_token, String username, T object );
	public T update(String api_access_token, String username, T object);
	public T delete(String api_access_token, String username, Integer key);
	public T get(Integer key);
	public SearchResults<T> search(Map<String, Object> postParams);
}
