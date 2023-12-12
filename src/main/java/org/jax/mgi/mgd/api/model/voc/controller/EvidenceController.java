package org.jax.mgi.mgd.api.model.voc.controller;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.domain.EvidenceDomain;
import org.jax.mgi.mgd.api.model.voc.service.EvidenceService;
import org.jax.mgi.mgd.api.util.SearchResults;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/vocevidence")
@Tag(name = "Voc Evidence Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EvidenceController extends BaseController<EvidenceDomain> {

	@Inject
	private EvidenceService evidenceService;

	@Override
	public SearchResults<EvidenceDomain> create(EvidenceDomain domain, User user) {
		return evidenceService.create(domain, user);
	}

	@Override
	public SearchResults<EvidenceDomain> update(EvidenceDomain domain, User user) {
		return evidenceService.update(domain, user);
	}

	@Override
	public EvidenceDomain get(Integer key) {
		return evidenceService.get(key);
	}

	@Override
	public SearchResults<EvidenceDomain> delete(Integer key, User user) {
		return evidenceService.delete(key, user);
	}
	
	@POST
	@Operation(description = "Process")
	@Path("/process")
	public Boolean process(String parentKey, List<EvidenceDomain> domain, String annotTypeKey, String mgiTypeKey, String noteTypeKey, User user) {
		return evidenceService.process(parentKey, domain, annotTypeKey, mgiTypeKey, user);
	}
		
}
