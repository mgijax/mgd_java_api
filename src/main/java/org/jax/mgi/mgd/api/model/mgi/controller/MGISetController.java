package org.jax.mgi.mgd.api.model.mgi.controller;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.mgi.domain.MGISetDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.service.MGISetService;
import org.jax.mgi.mgd.api.util.SearchResults;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/mgiset")
@Tag(name = "MGI Set Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MGISetController extends BaseController<MGISetDomain> {

	@Inject
	private MGISetService setService;

	@Override
	public SearchResults<MGISetDomain> create(MGISetDomain domain, User user) {
		return setService.create(domain, user);
	}

	@Override
	public SearchResults<MGISetDomain> update(MGISetDomain domain, User user) {
		SearchResults<MGISetDomain> results = new SearchResults<MGISetDomain>();
		results = setService.update(domain, user);	
		results = setService.getResults(Integer.valueOf(results.items.get(0).getSetKey()));
		return results;		}

	@Override
	public MGISetDomain get(Integer key) {
		return setService.get(key);
	}

	@Override
	public SearchResults<MGISetDomain> delete(Integer key, User user) {
		return setService.delete(key, user);
	}

	@POST
	@Operation(description = "Get Set Members by Set/User ordered by label")
	@Path("/getBySetUser")
	public List<MGISetDomain> getBySetUser(MGISetDomain domain) {
			
		List<MGISetDomain> results = new ArrayList<MGISetDomain>();
		
		try {
			results = setService.getBySetUser(domain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}

	@POST
	@Operation(description = "Get Set Members by Set/User ordered by seqNum")
	@Path("/getBySetUserBySeqNum")
	public List<MGISetDomain> getBySetUserBySeqNum(MGISetDomain domain) {
			
		List<MGISetDomain> results = new ArrayList<MGISetDomain>();
		
		try {
			results = setService.getBySetUserBySeqNum(domain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
}
