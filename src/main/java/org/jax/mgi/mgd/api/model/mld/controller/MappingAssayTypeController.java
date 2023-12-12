package org.jax.mgi.mgd.api.model.mld.controller;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mld.domain.MappingAssayTypeDomain;
import org.jax.mgi.mgd.api.model.mld.service.MappingAssayTypeService;
import org.jax.mgi.mgd.api.util.SearchResults;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/mappingassaytype")
@Tag(name = "Mapping Assay Type Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MappingAssayTypeController extends BaseController<MappingAssayTypeDomain> {

	@Inject
	private MappingAssayTypeService assayTypeService;

	@Override
	public SearchResults<MappingAssayTypeDomain> create(MappingAssayTypeDomain domain, User user) {
		SearchResults<MappingAssayTypeDomain> results = new SearchResults<MappingAssayTypeDomain>();
		results = assayTypeService.create(domain, user);
		results = assayTypeService.getResults(Integer.valueOf(results.items.get(0).getAssayTypeKey()));
		return results;
	}

	@Override
	public SearchResults<MappingAssayTypeDomain> update(MappingAssayTypeDomain domain, User user) {
		SearchResults<MappingAssayTypeDomain> results = new SearchResults<MappingAssayTypeDomain>();
		results = assayTypeService.update(domain, user);
		return results;
	}

	@Override
	public SearchResults<MappingAssayTypeDomain> delete(Integer key, User user) {
		return assayTypeService.delete(key, user);
	}
	
	@Override
	public MappingAssayTypeDomain get(Integer key) {
		return assayTypeService.get(key);
	}
		
	@POST
	@Operation(description = "Search/returns antibody class domain")
	@Path("/search")
	public List<MappingAssayTypeDomain> search(MappingAssayTypeDomain searchDomain) {
	
		List<MappingAssayTypeDomain> results = new ArrayList<MappingAssayTypeDomain>();

		try {
			results = assayTypeService.search(searchDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}	
	
}
