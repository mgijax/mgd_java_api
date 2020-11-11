package org.jax.mgi.mgd.api.model.bib.controller;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.bib.domain.ReferenceDomain;
import org.jax.mgi.mgd.api.model.bib.domain.SlimReferenceDomain;
import org.jax.mgi.mgd.api.model.bib.service.ReferenceService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Path("/reference")
@Api(value = "Reference Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReferenceController extends BaseController<ReferenceDomain> {

	@Inject
	private ReferenceService referenceService;

	@Override
	public SearchResults<ReferenceDomain> create(ReferenceDomain domain, User user) {
		SearchResults<ReferenceDomain> results = new SearchResults<ReferenceDomain>();
		results = referenceService.create(domain, user);
		results = referenceService.getResults(Integer.valueOf(results.items.get(0).getRefsKey()));
		return results;	}

	@Override
	public SearchResults<ReferenceDomain> update(ReferenceDomain domain, User user) {
		SearchResults<ReferenceDomain> results = new SearchResults<ReferenceDomain>();
		results = referenceService.update(domain, user);
		results = referenceService.getResults(Integer.valueOf(results.items.get(0).getRefsKey()));
		return results;		}

	@Override
	public ReferenceDomain get(Integer key) {
		return referenceService.get(key);
	}

	@Override
	public SearchResults<ReferenceDomain> delete(Integer key, User user) {
		return referenceService.delete(key, user);
	}

	@POST
	@ApiOperation(value = "Search/returns slim reference domain")
	@Path("/search")
	public List<SlimReferenceDomain> search(ReferenceDomain searchDomain) {
	
		List<SlimReferenceDomain> results = new ArrayList<SlimReferenceDomain>();

		try {
			results = referenceService.search(searchDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}

	@GET
	@ApiOperation(value = "get list of journals")
	@Path("/getJournalList")
	public SearchResults<String> getJournalList() {
	
		SearchResults<String> results = null;

		try {
			results = referenceService.getJournalList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//log.info(results);
		return results;
	}
	
	@GET
	@ApiOperation(value = "Validate reference by J:/returns slim reference domain")
	@Path("/validJnum/{jnum}")
	public List<SlimReferenceDomain> validJnum(
			@PathParam("jnum") 
			@ApiParam(value = "Validating jnum") 
			String jnum) {
		return referenceService.validJnum(jnum);
	}

	@POST
	@ApiOperation(value = "Validate reference, copyright, creative commons")
	@Path("/validateJnumImage")
	public List<SlimReferenceDomain> validateJnumImage(SlimReferenceDomain domain) {
		return referenceService.validateJnumImage(domain);
	}

}
