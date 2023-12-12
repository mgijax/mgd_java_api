package org.jax.mgi.mgd.api.model.voc.controller;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.domain.EvidencePropertyDomain;
import org.jax.mgi.mgd.api.model.voc.service.EvidencePropertyService;
import org.jax.mgi.mgd.api.util.SearchResults;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/vocevidenceproperty")
@Tag(name = "Voc Evidence Property Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EvidencePropertyController extends BaseController<EvidencePropertyDomain> {

	@Inject
	private EvidencePropertyService propertyService;

	@Override
	public SearchResults<EvidencePropertyDomain> create(EvidencePropertyDomain domain, User user) {
		return propertyService.create(domain, user);
	}

	@Override
	public SearchResults<EvidencePropertyDomain> update(EvidencePropertyDomain domain, User user) {
		return propertyService.update(domain, user);
	}

	@Override
	public EvidencePropertyDomain get(Integer key) {
		return propertyService.get(key);
	}

	@Override
	public SearchResults<EvidencePropertyDomain> delete(Integer key, User user) {
		return propertyService.delete(key, user);
	}
	
	@POST
	@Operation(description = "Process")
	@Path("/process")
	public Boolean process(String parentKey, List<EvidencePropertyDomain> domain, String annotTypeKey, String mgiTypeKey, String noteTypeKey, User user) {
		return propertyService.process(parentKey, domain, annotTypeKey, user);
	}
		
}
