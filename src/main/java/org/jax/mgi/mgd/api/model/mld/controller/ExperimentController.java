package org.jax.mgi.mgd.api.model.mld.controller;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.BaseSearchInterface;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mld.domain.ExperimentDomain;
import org.jax.mgi.mgd.api.model.mld.search.ExperimentSearchForm;
import org.jax.mgi.mgd.api.model.mld.service.ExperimentService;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;

@Path("/experiment")
@Api(value = "Experiment Endpoints", description="This is the description")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ExperimentController extends BaseController<ExperimentDomain> {

	@Inject
	private ExperimentService exptService;

	@Override
	public SearchResults<ExperimentDomain> create(ExperimentDomain expt, User user) {
		return exptService.create(expt, user);
	}

	@Override
	public SearchResults<ExperimentDomain> update(ExperimentDomain expt, User user) {
		return exptService.update(expt, user);
	}

	@Override
	public ExperimentDomain get(Integer key) {
		return exptService.get(key);
	}

	@Override
	public SearchResults<ExperimentDomain> delete(Integer key, User user) {
		return exptService.delete(key, user);
	}
	
}
