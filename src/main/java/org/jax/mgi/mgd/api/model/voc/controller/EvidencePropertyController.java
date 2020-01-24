package org.jax.mgi.mgd.api.model.voc.controller;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.domain.EvidencePropertyDomain;
import org.jax.mgi.mgd.api.model.voc.service.EvidencePropertyService;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/vocevidenceproperty")
@Api(value = "Voc Evidence Property Endpoints")
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
	@ApiOperation(value = "Process")
	@Path("/process")
	public Boolean process(String parentKey, List<EvidencePropertyDomain> domain, String annotTypeKey, String mgiTypeKey, String noteTypeKey, User user) {
		return propertyService.process(parentKey, domain, annotTypeKey, user);
	}
		
}
