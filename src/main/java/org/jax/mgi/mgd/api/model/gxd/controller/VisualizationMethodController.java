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
import org.jax.mgi.mgd.api.model.gxd.domain.VisualizationMethodDomain;
import org.jax.mgi.mgd.api.model.gxd.service.VisualizationMethodService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/gxdvisualization")
@Api(value = "GXD VisualizationMethod Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VisualizationMethodController extends BaseController<VisualizationMethodDomain> {

	@Inject
	private VisualizationMethodService visualizationService;

	@Override
	public SearchResults<VisualizationMethodDomain> create(VisualizationMethodDomain domain, User user) {
		SearchResults<VisualizationMethodDomain> results = new SearchResults<VisualizationMethodDomain>();
		results = visualizationService.create(domain, user);
		results = visualizationService.getResults(Integer.valueOf(results.items.get(0).getVocabKey()));
		return results;
	}

	@Override
	public SearchResults<VisualizationMethodDomain> update(VisualizationMethodDomain domain, User user) {
		SearchResults<VisualizationMethodDomain> results = new SearchResults<VisualizationMethodDomain>();
		results = visualizationService.update(domain, user);
		return results;
	}

	@Override
	public SearchResults<VisualizationMethodDomain> delete(Integer key, User user) {
		return visualizationService.delete(key, user);
	}
	
	@Override
	public VisualizationMethodDomain get(Integer key) {
		return visualizationService.get(key);
	}
		
	@POST
	@ApiOperation(value = "Search/returns domain")
	@Path("/search")
	public List<VisualizationMethodDomain> search(VisualizationMethodDomain searchDomain) {
	
		List<VisualizationMethodDomain> results = new ArrayList<VisualizationMethodDomain>();

		try {
			results = visualizationService.search(searchDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}	
	
}
