package org.jax.mgi.mgd.api.model.all.controller;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.BaseSearchInterface;
import org.jax.mgi.mgd.api.model.all.domain.AlleleDomain;
import org.jax.mgi.mgd.api.model.all.domain.AlleleEIResultDomain;
import org.jax.mgi.mgd.api.model.all.search.AlleleSearchForm;
import org.jax.mgi.mgd.api.model.all.service.AlleleService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/allele")
@Api(value = "Allele Endpoints", description="This is the description")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AlleleController extends BaseController<AlleleDomain> {

	@Inject
	private AlleleService alleleService;

	@Override
	public SearchResults<AlleleDomain> create(AlleleDomain allele, User user) {
		return alleleService.create(allele, user);
	}

	@Override
	public SearchResults<AlleleDomain> update(AlleleDomain allele, User user) {
		return alleleService.update(allele, user);
	}

	@Override
	public AlleleDomain get(Integer alleleKey) {
		return alleleService.get(alleleKey);
	}

	@Override
	public SearchResults<AlleleDomain> delete(Integer key, User user) {
		return alleleService.delete(key, user);
	}
	
	//@POST
	//@ApiOperation(value = "EI Allele Search")
	//@Path("/eiSearch")
	//public AlleleEIResultDomain eiSearch(AlleleSearchForm searchForm) {
		//return alleleService.eiSearch(searchForm);
	//}

}
