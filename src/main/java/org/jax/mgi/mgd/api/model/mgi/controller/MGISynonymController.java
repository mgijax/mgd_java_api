package org.jax.mgi.mgd.api.model.mgi.controller;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.BaseSearchInterface;
import org.jax.mgi.mgd.api.model.mgi.domain.MGISynonymDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.search.MGISynonymSearchForm;
import org.jax.mgi.mgd.api.model.mgi.service.MGISynonymService;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;

@Path("/mgisynonym")
@Api(value = "MGI Synonym Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MGISynonymController extends BaseController<MGISynonymDomain> implements BaseSearchInterface<MGISynonymDomain, MGISynonymSearchForm> {

	@Inject
	private MGISynonymService synonymService;

	public MGISynonymDomain create(MGISynonymDomain synonym, User user) {
		try {
			return synonymService.create(synonym, user);
		} catch (APIException e) {
			e.printStackTrace();
			return null;
		}
	}

	public MGISynonymDomain update(MGISynonymDomain synonym, User user) {
		return synonymService.update(synonym, user);
	}

	public MGISynonymDomain get(Integer key) {
		return synonymService.get(key);
	}

	public SearchResults<MGISynonymDomain> delete(Integer key, User user) {
		return synonymService.delete(key, user);
	}
	
    @Override
	public SearchResults<MGISynonymDomain> search(MGISynonymSearchForm searchForm) {
		return synonymService.search(searchForm);
	}

}
