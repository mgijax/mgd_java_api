package org.jax.mgi.mgd.api.model.gxd.controller;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.gxd.domain.HTSampleDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.SlimHTDomain;
import org.jax.mgi.mgd.api.model.gxd.service.HTSampleService;
import org.jax.mgi.mgd.api.model.mgi.domain.MGISetMemberCellTypeDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.MGISetMemberEmapaDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.SearchResults;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/htsample")
@Tag(name = "HT Sample Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HTSampleController extends BaseController<HTSampleDomain> {

	@Inject
	private HTSampleService htSampleService;

	@Override
	public SearchResults<HTSampleDomain> create(HTSampleDomain domain, User user) {
		SearchResults<HTSampleDomain> results = new SearchResults<HTSampleDomain>();
		results = htSampleService.create(domain, user);
		results = htSampleService.getResults(Integer.valueOf(results.items.get(0).get_sample_key()));
		return results;
	}

	@Override
	public SearchResults<HTSampleDomain> update(HTSampleDomain domain, User user) {
		SearchResults<HTSampleDomain> results = new SearchResults<HTSampleDomain>();
		results = htSampleService.update(domain, user);
		results = htSampleService.getResults(Integer.valueOf(results.items.get(0).get_sample_key()));
		return results;
	}

	@Override
	public SearchResults<HTSampleDomain> delete(Integer key, User user) {
		return htSampleService.delete(key, user);
	}
	
	@Override
	public HTSampleDomain get(Integer key) {
		return htSampleService.get(key);
	}

//	@GET
//	@Operation(description = "Get the object count from gxd_antibody table")
//	@Path("/getObjectCount")
//	public SearchResults<HTSampleDomain> getObjectCount() {
//		return htSampleService.getObjectCount();
//	}

	@POST
	@Operation(description = "Get CellType Set Members by HT Sample and Set/User")
	@Path("/getCellTypeHTSampleBySetUser")
	public List<MGISetMemberCellTypeDomain> getCellTypeHTSampleBySetUser(SlimHTDomain domain) {
			
		List<MGISetMemberCellTypeDomain> results = new ArrayList<MGISetMemberCellTypeDomain>();
		
		try {
			results = htSampleService.getCellTypeHTSampleBySetUser(domain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
	@POST
	@Operation(description = "Get EMAPA Set Members by HT Sample and Set/User")
	@Path("/getEmapaHTSampleBySetUser")
	public List<MGISetMemberEmapaDomain> getEmapaHTSampleBySetUser(SlimHTDomain domain) {
			
		List<MGISetMemberEmapaDomain> results = new ArrayList<MGISetMemberEmapaDomain>();
		
		try {
			results = htSampleService.getEmapaHTSampleBySetUser(domain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
}
