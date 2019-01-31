package org.jax.mgi.mgd.api.model.mgi.controller;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.mgi.domain.MGISynonymDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.service.MGISynonymService;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/mgisynonym")
@Api(value = "MGI Synonym Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MGISynonymController extends BaseController<MGISynonymDomain> {

	@Inject
	private MGISynonymService synonymService;

	@Override
	public SearchResults<MGISynonymDomain> create(MGISynonymDomain domain, User user) {
		return synonymService.create(domain, user);
	}

	@Override
	public SearchResults<MGISynonymDomain> update(MGISynonymDomain domain, User user) {
		return synonymService.update(domain, user);
	}

	@Override
	public MGISynonymDomain get(Integer key) {
		return synonymService.get(key);
	}

	@Override
	public SearchResults<MGISynonymDomain> delete(Integer key, User user) {
		return synonymService.delete(key, user);
	}
	
	@POST
	@ApiOperation(value = "Get All Synonyms by Marker")
	@Path("/marker")
	public List<MGISynonymDomain> getByMarker(Integer key) {
			
		List<MGISynonymDomain> results = new ArrayList<MGISynonymDomain>();
		
		try {
			results = synonymService.getByMarker(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
	@POST
	@ApiOperation(value = "Process")
	@Path("/process")
	public void process(String parentKey, List<MGISynonymDomain> domain, String mgiTypeKey, User user) {
		synonymService.process(parentKey, domain, mgiTypeKey, user);
		return;
	}
		
}
