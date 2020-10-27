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
import org.jax.mgi.mgd.api.model.gxd.domain.AntibodyClassDomain;
import org.jax.mgi.mgd.api.model.gxd.service.AntibodyClassService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/antibodyclass")
@Api(value = "Antibody Class Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AntibodyClassController extends BaseController<AntibodyClassDomain> {

	@Inject
	private AntibodyClassService antibodyclassService;

	@Override
	public SearchResults<AntibodyClassDomain> create(AntibodyClassDomain domain, User user) {
		SearchResults<AntibodyClassDomain> results = new SearchResults<AntibodyClassDomain>();
		results = antibodyclassService.create(domain, user);
		results = antibodyclassService.getResults(Integer.valueOf(results.items.get(0).getVocabKey()));
		return results;
	}

	@Override
	public SearchResults<AntibodyClassDomain> update(AntibodyClassDomain domain, User user) {
		SearchResults<AntibodyClassDomain> results = new SearchResults<AntibodyClassDomain>();
		results = antibodyclassService.update(domain, user);
		results.setItems(antibodyclassService.search(domain));
		return results;
	}

	@Override
	public SearchResults<AntibodyClassDomain> delete(Integer key, User user) {
		return antibodyclassService.delete(key, user);
	}
	
	@Override
	public AntibodyClassDomain get(Integer key) {
		return antibodyclassService.get(key);
	}
		
	@POST
	@ApiOperation(value = "Search/returns antibody class domain")
	@Path("/search")
	public List<AntibodyClassDomain> search(AntibodyClassDomain searchDomain) {
	
		List<AntibodyClassDomain> results = new ArrayList<AntibodyClassDomain>();

		try {
			results = antibodyclassService.search(searchDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}	
	
}
