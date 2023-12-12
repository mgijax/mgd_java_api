package org.jax.mgi.mgd.api.model.mgi.controller;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.mgi.domain.MGIReferenceAlleleAssocDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.MGIReferenceAssocDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.MGIReferenceDOIDAssocDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.MGIReferenceMarkerAssocDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.MGIReferenceStrainAssocDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.service.MGIReferenceAssocService;
import org.jax.mgi.mgd.api.util.SearchResults;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/mgireferenceassoc")
@Tag(name = "MGI Reference Assoc Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MGIReferenceAssocController extends BaseController<MGIReferenceAssocDomain> {

	@Inject
	private MGIReferenceAssocService referenceAssocService;

	@Override
	public SearchResults<MGIReferenceAssocDomain> create(MGIReferenceAssocDomain domain, User user) {
		SearchResults<MGIReferenceAssocDomain> results = new SearchResults<MGIReferenceAssocDomain>();
		results = referenceAssocService.create(domain, user);
		results = referenceAssocService.getResults(Integer.valueOf(results.items.get(0).getAssocKey()));
		return results;
	}

	@Override
	public SearchResults<MGIReferenceAssocDomain> update(MGIReferenceAssocDomain domain, User user) {
		return referenceAssocService.update(domain, user);
	}

	@Override
	public MGIReferenceAssocDomain get(Integer key) {
		return referenceAssocService.get(key);
	}

	@Override
	public SearchResults<MGIReferenceAssocDomain> delete(Integer key, User user) {
		return referenceAssocService.delete(key, user);
	}
	
	@GET
	@Operation(description = "Get Reference Associations by Marker key")
	@Path("/marker/{key}")
	public List<MGIReferenceAssocDomain> getByMarker(@PathParam("key") Integer key) {
			
		List<MGIReferenceAssocDomain> results = new ArrayList<MGIReferenceAssocDomain>();
		
		try {
			results = referenceAssocService.getByMarker(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}

	@GET
	@Operation(description = "Get Allele Associations by Reference key")
	@Path("/alleleByReference/{key}")
	public List<MGIReferenceAlleleAssocDomain> getAlleles(@PathParam("key") Integer key) {
		
		List<MGIReferenceAlleleAssocDomain> results = new ArrayList<MGIReferenceAlleleAssocDomain>();
		
		try {
			results = referenceAssocService.getAlleles(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}

	@GET
	@Operation(description = "Get Marker Associations by Reference key")
	@Path("/markerByReference/{key}")
	public List<MGIReferenceMarkerAssocDomain> getMarker(@PathParam("key") Integer key) {
		
		List<MGIReferenceMarkerAssocDomain> results = new ArrayList<MGIReferenceMarkerAssocDomain>();
		
		try {
			results = referenceAssocService.getMarkers(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
		
	@GET
	@Operation(description = "Get Strain Associations by Reference key")
	@Path("/strainByReference/{key}")
	public List<MGIReferenceStrainAssocDomain> getStrains(@PathParam("key") Integer key) {
		
		List<MGIReferenceStrainAssocDomain> results = new ArrayList<MGIReferenceStrainAssocDomain>();
		
		try {
			results = referenceAssocService.getStrains(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}

	@GET
	@Operation(description = "Get DOID Associations by Reference key")
	@Path("/doidByReference/{key}")
	public List<MGIReferenceDOIDAssocDomain> getDOIDs(@PathParam("key") Integer key) {
		
		List<MGIReferenceDOIDAssocDomain> results = new ArrayList<MGIReferenceDOIDAssocDomain>();
		
		try {
			results = referenceAssocService.getDOIDs(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
}
