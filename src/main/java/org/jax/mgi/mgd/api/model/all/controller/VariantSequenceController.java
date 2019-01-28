package org.jax.mgi.mgd.api.model.all.controller;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.all.domain.VariantSequenceDomain;
import org.jax.mgi.mgd.api.model.all.service.VariantSequenceService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mrk.domain.MarkerHistoryDomain;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/allelevariantsequence")
@Api(value = "Allele Variant Sequence Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VariantSequenceController extends BaseController<VariantSequenceDomain> {

	@Inject
	private VariantSequenceService variantSequenceService;

	@Override
	public SearchResults<VariantSequenceDomain> create(VariantSequenceDomain domain, User user) {		
		return variantSequenceService.create(domain, user);
	}

	@Override
	public SearchResults<VariantSequenceDomain> update(VariantSequenceDomain domain, User user) {
		return variantSequenceService.update(domain, user);
	}

	@Override
	public VariantSequenceDomain get(Integer key) {
		return variantSequenceService.get(key);
	}

	@Override
	public SearchResults<VariantSequenceDomain> delete(Integer key, User user) {
		return variantSequenceService.delete(key, user);
	}
	
	@POST
	@ApiOperation(value = "Search")
	@Path("/search")
	public List<VariantSequenceDomain> search(VariantSequenceDomain searchDomain) {
		return variantSequenceService.search(searchDomain);
	}
	@POST
	@ApiOperation(value = "Process")
	@Path("/process")
	public void process(String parentKey, List<VariantSequenceDomain> domain, User user) {
		variantSequenceService.process(parentKey, domain, user);
		return;
	}
	
}
