package org.jax.mgi.mgd.api.model.mgi.controller;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.BaseSearchInterface;
import org.jax.mgi.mgd.api.model.mgi.domain.OrganismDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.search.OrganismSearchForm;
import org.jax.mgi.mgd.api.model.mgi.service.OrganismService;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;

@Path("/organism")
@Api(value = "Organism Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrganismController extends BaseController<OrganismDomain> implements BaseSearchInterface<OrganismDomain, OrganismSearchForm> {

	@Inject
	private OrganismService organismService;

	public OrganismDomain create(OrganismDomain organism, User user) {
		try {
			return organismService.create(organism, user);
		} catch (APIException e) {
			e.printStackTrace();
			return null;
		}
	}

	public OrganismDomain update(OrganismDomain organism, User user) {
		return organismService.update(organism, user);
	}

	public OrganismDomain get(Integer key) {
		return organismService.get(key);
	}

	public OrganismDomain delete(Integer organism_key, User user) {
		return organismService.delete(organism_key, user);
	}

	@Override
	public SearchResults<OrganismDomain> search(OrganismSearchForm searchForm) {
		return organismService.search(searchForm);
	}

}
