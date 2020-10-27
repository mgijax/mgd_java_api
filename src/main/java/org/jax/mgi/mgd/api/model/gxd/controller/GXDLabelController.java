package org.jax.mgi.mgd.api.model.gxd.controller;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.gxd.domain.GXDLabelDomain;
import org.jax.mgi.mgd.api.model.gxd.service.GXDLabelService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/gxdlabel")
@Api(value = "GXD Label Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GXDLabelController extends BaseController<GXDLabelDomain> {

	@Inject
	private GXDLabelService gxdLabelService;

	@Override
	public SearchResults<GXDLabelDomain> create(GXDLabelDomain domain, User user) {
		SearchResults<GXDLabelDomain> results = new SearchResults<GXDLabelDomain>();
		results = gxdLabelService.create(domain, user);
		results = gxdLabelService.getResults(Integer.valueOf(results.items.get(0).getVocabKey()));
		return results;
	}

	@Override
	public SearchResults<GXDLabelDomain> update(GXDLabelDomain domain, User user) {
		SearchResults<GXDLabelDomain> results = new SearchResults<GXDLabelDomain>();
		results = gxdLabelService.update(domain, user);
		results.setItems(gxdLabelService.search(domain));
		return results;
	}

	@Override
	public SearchResults<GXDLabelDomain> delete(Integer key, User user) {
		return gxdLabelService.delete(key, user);
	}
	
	@Override
	public GXDLabelDomain get(Integer key) {
		return gxdLabelService.get(key);
	}
		
	@POST
	@ApiOperation(value = "Search/returns domain")
	@Path("/search")
	public List<GXDLabelDomain> search(GXDLabelDomain searchDomain) {
	
		List<GXDLabelDomain> results = new ArrayList<GXDLabelDomain>();

		try {
			results = gxdLabelService.search(searchDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}	
	
}
