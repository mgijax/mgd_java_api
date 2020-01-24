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
import org.jax.mgi.mgd.api.model.voc.domain.EvidenceDomain;
import org.jax.mgi.mgd.api.model.voc.service.EvidenceService;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/vocevidence")
@Api(value = "Voc Evidence Endpoints")
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
	@ApiOperation(value = "Process")
	@Path("/process")
	public Boolean process(String parentKey, List<EvidenceDomain> domain, String annotTypeKey, String mgiTypeKey, String noteTypeKey, User user) {
		return evidenceService.process(parentKey, domain, annotTypeKey, mgiTypeKey, user);
	}
		
}
