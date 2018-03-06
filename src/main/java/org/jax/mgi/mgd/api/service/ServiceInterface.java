package org.jax.mgi.mgd.api.service;

import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.model.DomainBase;
import org.jax.mgi.mgd.api.model.SearchForm;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.SearchResults;

import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class ServiceInterface<D extends DomainBase, S extends SearchForm> {
	
	protected ObjectMapper mapper = new ObjectMapper();
	
	public abstract D create(D object, User user) throws APIException;
	public abstract D update(D object, User user);
	public abstract D get(Integer key);
	public abstract D delete(Integer key, User user);
	public abstract SearchResults<D> search(S searchForm);

}
