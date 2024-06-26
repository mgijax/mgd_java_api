package org.jax.mgi.mgd.api.model.gxd.controller;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.gxd.domain.AntibodyAliasDomain;
import org.jax.mgi.mgd.api.model.gxd.service.AntibodyAliasService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.SearchResults;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/antibodyalias")
@Tag(name = "Antibody Alias Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AntibodyAliasController extends BaseController<AntibodyAliasDomain> {

	@Inject
	private AntibodyAliasService antibodyAliasService;

	@Override
	public SearchResults<AntibodyAliasDomain> create(AntibodyAliasDomain domain, User user) {
		SearchResults<AntibodyAliasDomain> results = new SearchResults<AntibodyAliasDomain>();
		results = antibodyAliasService.create(domain, user);
		results = antibodyAliasService.getResults(Integer.valueOf(results.items.get(0).getAntibodyKey()));
		return results;
	}

	@Override
	public SearchResults<AntibodyAliasDomain> update(AntibodyAliasDomain domain, User user) {
		SearchResults<AntibodyAliasDomain> results = new SearchResults<AntibodyAliasDomain>();
		results = antibodyAliasService.update(domain, user);
		results = antibodyAliasService.getResults(Integer.valueOf(results.items.get(0).getAntibodyKey()));
		return results;
	}

	@Override
	public SearchResults<AntibodyAliasDomain> delete(Integer key, User user) {
		return antibodyAliasService.delete(key, user);
	}
	
	@Override
	public AntibodyAliasDomain get(Integer key) {
		return antibodyAliasService.get(key);
	}

	@GET
	@Operation(description = "Get the object count from gxd_antibodyalias table")
	@Path("/getObjectCount")
	public SearchResults<AntibodyAliasDomain> getObjectCount() {
		return antibodyAliasService.getObjectCount();
	}
		
	@POST
	@Operation(description = "Search/returns antibody alias domain")
	@Path("/search")
	public List<AntibodyAliasDomain> search(AntibodyAliasDomain searchDomain) {
	
		List<AntibodyAliasDomain> results = new ArrayList<AntibodyAliasDomain>();

		try {
			results = antibodyAliasService.search(searchDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}	
	
}
