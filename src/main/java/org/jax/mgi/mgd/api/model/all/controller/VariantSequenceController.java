package org.jax.mgi.mgd.api.model.all.controller;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.all.domain.VariantSequenceDomain;
import org.jax.mgi.mgd.api.model.all.service.VariantSequenceService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.SearchResults;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/allelevariantsequence")
@Schema(description = "Allele Variant Sequence Endpoints")
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
	@Operation(description = "Search")
	@Path("/search")
	public List<VariantSequenceDomain> search(VariantSequenceDomain searchDomain) {
		return variantSequenceService.search(searchDomain);
	}
	@POST
	@Operation(description = "Process")
	@Path("/process")
	public void process(String parentKey, List<VariantSequenceDomain> domain, User user) {
		variantSequenceService.process(parentKey, domain, user);
		return;
	}
	
}
