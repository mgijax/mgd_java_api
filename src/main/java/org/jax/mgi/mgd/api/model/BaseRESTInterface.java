package org.jax.mgi.mgd.api.model;

import org.jax.mgi.mgd.api.exception.APIException;

public interface BaseRESTInterface<T extends BaseDomain> {
	public T create(String api_access_token, String username, T object ) throws APIException;
	public T update(String api_access_token, String username, T object) throws APIException;
	public T delete(String api_access_token, String username, Integer key) throws APIException;
	public T getByKey(Integer key);
}
