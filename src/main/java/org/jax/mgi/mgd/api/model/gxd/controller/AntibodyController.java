package org.jax.mgi.mgd.api.model.gxd.controller;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.BaseSearchInterface;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.gxd.domain.AntibodyDomain;
import org.jax.mgi.mgd.api.model.gxd.search.AntibodySearchForm;
import org.jax.mgi.mgd.api.model.gxd.service.AntibodyService;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;

@Path("/antibody")
@Api(value = "Antibody Endpoints", description="This is the description")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AntibodyController extends BaseController<AntibodyDomain> {

	@Inject
	private AntibodyService antibodyService;

	@Override
	public SearchResults<AntibodyDomain> create(AntibodyDomain antibody, User user) {
		return antibodyService.create(antibody, user);
	}

	@Override
	public SearchResults<AntibodyDomain> update(AntibodyDomain antibody, User user) {
		return antibodyService.update(antibody, user);
	}

	@Override
	public AntibodyDomain get(Integer antibodyKey) {
		return antibodyService.get(antibodyKey);
	}

	@Override
	public SearchResults<AntibodyDomain> delete(Integer key, User user) {
		return antibodyService.delete(key, user);
	}
	
}
