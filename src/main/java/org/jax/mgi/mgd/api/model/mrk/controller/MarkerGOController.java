package org.jax.mgi.mgd.api.model.mrk.controller;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mrk.domain.DenormMarkerAnnotDomain;
import org.jax.mgi.mgd.api.model.mrk.domain.MarkerGOReferenceDomain;
import org.jax.mgi.mgd.api.model.mrk.domain.SlimMarkerAnnotDomain;
import org.jax.mgi.mgd.api.model.mrk.domain.SlimMarkerDomain;
import org.jax.mgi.mgd.api.model.mrk.service.MarkerAnnotService;
import org.jax.mgi.mgd.api.model.voc.domain.DenormAnnotationDomain;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/markerGOannot")
@Api(value = "Marker GO Annotations Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MarkerGOController extends BaseController<DenormMarkerAnnotDomain> {

	protected Logger log = Logger.getLogger(getClass());
	ObjectMapper mapper = new ObjectMapper();
	String annotType = "1000";
	
	@Inject
	private MarkerAnnotService markerAnnotService;

	// refresh/resync the results due to database triggers
	// for example, the mgi accession id is created by a database trigger
	
	@Override
	public SearchResults<DenormMarkerAnnotDomain> create(DenormMarkerAnnotDomain domain, User user) {	
		log.info("MarkerGOController.create");
		SearchResults<DenormMarkerAnnotDomain> results = new SearchResults<DenormMarkerAnnotDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);	
		return results;
	}

	@Override
	public SearchResults<DenormMarkerAnnotDomain> update(DenormMarkerAnnotDomain domain, User user) {
		SearchResults<DenormMarkerAnnotDomain> results = new SearchResults<DenormMarkerAnnotDomain>();
		results = markerAnnotService.update(domain, user);
		return results;	
	}

	@Override
	public DenormMarkerAnnotDomain get(Integer markerKey) {
		return markerAnnotService.get(markerKey, Integer.valueOf(annotType));
	}

	@Override
	public SearchResults<DenormMarkerAnnotDomain> delete(Integer key, User user) {
		log.info("MarkerGOController.delete");
		SearchResults<DenormMarkerAnnotDomain> results = new SearchResults<DenormMarkerAnnotDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;
	}

	@GET
	@ApiOperation(value = "Get the object count from voc_annot table where _annottype_key = 1021")
	@Path("/getObjectCount")
	public SearchResults<DenormMarkerAnnotDomain> getObjectCount() {
		return markerAnnotService.getObjectCount(annotType);
	}
	
	@POST
	@ApiOperation(value = "Get ordered searh results")
	@Path("/getOrderBy")
	public SearchResults<DenormAnnotationDomain> getOrderBy(DenormMarkerAnnotDomain domain) {
		return markerAnnotService.getOrderBy(domain);
	}
		
	@POST
	@ApiOperation(value = "Search/returns slim marker domain")
	@Path("/search")
	public List<SlimMarkerAnnotDomain> search(DenormMarkerAnnotDomain searchDomain) {
	
		List<SlimMarkerAnnotDomain> results = new ArrayList<SlimMarkerAnnotDomain>();

		try {
			results = markerAnnotService.search(searchDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}

	@GET
	@ApiOperation(value = "Get references not coded for GO by marker key")
	@Path("/getReferences/{key}")
	public List<MarkerGOReferenceDomain> getReferences(@PathParam("key") Integer key) {
		return markerAnnotService.getGOReferences(key);
	}

	@POST
	@ApiOperation(value = "Get GO reference report by marker key")
	@Path("/getReferenceReport")
	public List<SlimMarkerDomain> getReferenceReport(SlimMarkerDomain domain) {
	
		List<SlimMarkerDomain> results = new ArrayList<SlimMarkerDomain>();

		try {
			results = markerAnnotService.getGOReferenceReport(domain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
}
