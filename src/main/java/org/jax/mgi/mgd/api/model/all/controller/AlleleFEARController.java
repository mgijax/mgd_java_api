package org.jax.mgi.mgd.api.model.all.controller;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.all.domain.AlleleFEARDomain;
import org.jax.mgi.mgd.api.model.all.domain.SlimAlleleFEARDomain;
import org.jax.mgi.mgd.api.model.all.service.AlleleFEARService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/allelefear")
@Api(value = "Allele FEAR Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AlleleFEARController extends BaseController<AlleleFEARDomain> {

	protected Logger log = Logger.getLogger(getClass());
	ObjectMapper mapper = new ObjectMapper();
	
	@Inject
	private AlleleFEARService allelefearService;

	// refresh/resync the results due to database triggers
	// for example, the mgi accession id is created by a database trigger
	
	@Override
	public SearchResults<AlleleFEARDomain> create(AlleleFEARDomain domain, User user) {	
		SearchResults<AlleleFEARDomain> results = new SearchResults<AlleleFEARDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);	
		return results;
	}

	@Override
	public SearchResults<AlleleFEARDomain> update(AlleleFEARDomain domain, User user) {
		SearchResults<AlleleFEARDomain> results = new SearchResults<AlleleFEARDomain>();
		results = allelefearService.update(domain, user);
		return results;	
	}

	@Override
	public AlleleFEARDomain get(Integer key) {
		return allelefearService.get(Integer.valueOf(key));
	}

	@Override
	public SearchResults<AlleleFEARDomain> delete(Integer key, User user) {
		SearchResults<AlleleFEARDomain> results = new SearchResults<AlleleFEARDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@GET
	@ApiOperation(value = "Get the object count from mgi_relationship_fear_view by allele key")
	@Path("/getObjectCount")
	public SearchResults<AlleleFEARDomain> getObjectCount(Integer key) {
		return allelefearService.getObjectCount(key);
	}
		
	@POST
	@ApiOperation(value = "Search/returns slim marker domain")
	@Path("/search")
	public List<SlimAlleleFEARDomain> search(AlleleFEARDomain searchDomain) {
	
		List<SlimAlleleFEARDomain> results = new ArrayList<SlimAlleleFEARDomain>();

		try {
			results = allelefearService.search(searchDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}

}
