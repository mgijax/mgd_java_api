package org.jax.mgi.mgd.api.model.mgi.controller;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.mgi.domain.RelationshipDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.RelationshipFEARDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.service.RelationshipService;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/mgirelationship")
@Api(value = "MGI Relationship Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RelationshipController extends BaseController<RelationshipDomain> {

	@Inject
	private RelationshipService relationshipService;

	@Override
	public SearchResults<RelationshipDomain> create(RelationshipDomain domain, User user) {
		return relationshipService.create(domain, user);
	}

	@Override
	public SearchResults<RelationshipDomain> update(RelationshipDomain domain, User user) {
		return relationshipService.update(domain, user);
	}

	@Override
	public RelationshipDomain get(Integer key) {
		return relationshipService.get(key);
	}

	@Override
	public SearchResults<RelationshipDomain> delete(Integer key, User user) {
		return relationshipService.delete(key, user);
	}
	
	@POST
	@ApiOperation(value = "Get Marker-TSS by Marker")
	@Path("/markertss")
	public List<RelationshipDomain> getMarkerTSS(Integer key) {
			
		List<RelationshipDomain> results = new ArrayList<RelationshipDomain>();
		
		try {
			results = relationshipService.getMarkerTSS(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}

	@POST
	@ApiOperation(value = "Get FEAR (Allele/Marker)")
	@Path("/allelefear")
	public List<RelationshipFEARDomain> getAlleleFEAR(Integer key) {
			
		List<RelationshipFEARDomain> results = new ArrayList<RelationshipFEARDomain>();
		
		try {
			results = relationshipService.getAlleleFEAR(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
}
