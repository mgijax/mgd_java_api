package org.jax.mgi.mgd.api.model.mgi.controller;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.mgi.domain.MGISynonymDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.service.MGISynonymService;
import org.jax.mgi.mgd.api.util.SearchResults;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/mgisynonym")
@Tag(name = "MGI Synonym Endpoints")
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
	@Operation(description = "Get All Synonyms by Marker")
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
		
}
