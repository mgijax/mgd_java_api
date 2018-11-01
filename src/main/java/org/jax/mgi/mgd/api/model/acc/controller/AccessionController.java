package org.jax.mgi.mgd.api.model.acc.controller;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.BaseSearchInterface;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.acc.domain.AccessionDomain;
import org.jax.mgi.mgd.api.model.acc.search.AccessionSearchForm;
import org.jax.mgi.mgd.api.model.acc.service.AccessionService;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;

@Path("/accession")
@Api(value = "Accession Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AccessionController extends BaseController<AccessionDomain> implements BaseSearchInterface<AccessionDomain, AccessionSearchForm> {

	@Inject
	private AccessionService accessionService;

	@Override
	public SearchResults<AccessionDomain> create(AccessionDomain accession, User user) {
		return accessionService.create(accession, user);
	}

	@Override
	public SearchResults<AccessionDomain> update(AccessionDomain accession, User user) {
		return accessionService.update(accession, user);
	}

	@Override
	public AccessionDomain get(Integer accessionKey) {
		return accessionService.get(accessionKey);
	}

	@Override
	public SearchResults<AccessionDomain> delete(Integer key, User user) {
		return accessionService.delete(key, user);
	}
	
	@Override
	public SearchResults<AccessionDomain> search(AccessionSearchForm searchForm) {
		return accessionService.search(searchForm);
	}

}
