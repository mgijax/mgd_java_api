package org.jax.mgi.mgd.api.model;

import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.SearchResults;

import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class BaseService<D extends BaseDomain> {
	
	protected ObjectMapper mapper = new ObjectMapper();
	
	public abstract SearchResults<D> create(D object, User user) throws APIException;
	public abstract SearchResults<D> update(D object, User user);
	public abstract D get(Integer key);
	public abstract SearchResults<D> getResults(Integer key);
	public abstract SearchResults<D> delete(Integer key, User user);

}
