package org.jax.mgi.mgd.api.model.mgi.controller;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.mgi.domain.RelationshipDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.RelationshipFearDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.service.RelationshipService;
import org.jax.mgi.mgd.api.util.SearchResults;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/mgirelationship")
@Tag(name = "MGI Relationship Endpoints")
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
	@Operation(description = "Get Marker-TSS by Marker")
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
	@Operation(description = "Get Allele/Marker (Fear)")
	@Path("/allelefear")
	public List<RelationshipFearDomain> getAlleleFear(Integer key) {
			
		List<RelationshipFearDomain> results = new ArrayList<RelationshipFearDomain>();
		
		try {
			results = relationshipService.getAlleleFear(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
}
