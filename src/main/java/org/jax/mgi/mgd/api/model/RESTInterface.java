package org.jax.mgi.mgd.api.model;

public interface RESTInterface<T extends DomainBase> {
	public T create(String api_access_token, String username, T object );
	public T update(String api_access_token, String username, T object);
	public T delete(String api_access_token, String username, Integer key);
	public T getByKey(Integer key);
}
