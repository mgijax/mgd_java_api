package org.jax.mgi.mgd.api.model.mgi.controller;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.mgi.domain.MGIReferenceAlleleAssocDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.MGIReferenceAssocDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.MGIReferenceMarkerAssocDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.MGIReferenceStrainAssocDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.service.MGIReferenceAssocService;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/mgireferenceassoc")
@Api(value = "MGI Reference Assoc Endpoints")
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
	@ApiOperation(value = "Get Reference Associations by Marker key")
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
	@ApiOperation(value = "Get Allele Associations by Reference key")
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
	@ApiOperation(value = "Get Marker Associations by Reference key")
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
	@ApiOperation(value = "Get Strain Associations by Reference key")
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

}
