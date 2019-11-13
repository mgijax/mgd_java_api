package org.jax.mgi.mgd.api.model.all.controller;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.all.domain.DenormAlleleAnnotDomain;
import org.jax.mgi.mgd.api.model.all.domain.SlimAlleleDomain;
import org.jax.mgi.mgd.api.model.all.domain.SlimAlleleRefAssocDomain;
import org.jax.mgi.mgd.api.model.all.service.AlleleAnnotService;
import org.jax.mgi.mgd.api.model.mgi.domain.MGIReferenceAssocDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/alleleDOannot")
@Api(value = "Allele DO Annotations Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AlleleDOController extends BaseController<DenormAlleleAnnotDomain> {

	protected Logger log = Logger.getLogger(getClass());
	ObjectMapper mapper = new ObjectMapper();
	String annotType = "1021";
	
	@Inject
	private AlleleAnnotService alleleAnnotService;

	// refresh/resync the results due to database triggers
	// for example, the mgi accession id is created by a database trigger
	
	@Override
	public SearchResults<DenormAlleleAnnotDomain> create(DenormAlleleAnnotDomain domain, User user) {	
		log.info("AlleleDOController.create");
		SearchResults<DenormAlleleAnnotDomain> results = new SearchResults<DenormAlleleAnnotDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);	
		return results;
	}

	@Override
	public SearchResults<DenormAlleleAnnotDomain> update(DenormAlleleAnnotDomain domain, User user) {
		SearchResults<DenormAlleleAnnotDomain> results = new SearchResults<DenormAlleleAnnotDomain>();
		results = alleleAnnotService.update(domain, user);
		
		return results;	
	}

	@Override
	public DenormAlleleAnnotDomain get(Integer genotypeKey) {
		return alleleAnnotService.get(genotypeKey, Integer.valueOf(annotType));
	}

	@Override
	public SearchResults<DenormAlleleAnnotDomain> delete(Integer key, User user) {
		log.info("AlleleDOController.delete");
		SearchResults<DenormAlleleAnnotDomain> results = new SearchResults<DenormAlleleAnnotDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;		
	}

	@GET
	@ApiOperation(value = "Get the object count from voc_annot table where _annottype_key = 1021")
	@Path("/getObjectCount")
	public SearchResults<DenormAlleleAnnotDomain> getObjectCount() {
		return alleleAnnotService.getObjectCount(annotType);
	}
		
	@POST
	@ApiOperation(value = "Search/returns slim genotype domain")
	@Path("/search")
	public List<SlimAlleleDomain> search(DenormAlleleAnnotDomain searchDomain) {
	
		List<SlimAlleleDomain> results = new ArrayList<SlimAlleleDomain>();

		try {
			results = alleleAnnotService.search(searchDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}

	@POST
	@ApiOperation(value = "Validate Allele-Reference associations for Genotype")
	@Path("/validateAlleleReference")
	public List<MGIReferenceAssocDomain> validateAlleleReference(SlimAlleleRefAssocDomain searchDomain) {
		
		List<MGIReferenceAssocDomain> results = new ArrayList<MGIReferenceAssocDomain>();

		try {
			results = alleleAnnotService.validateAlleleReference(searchDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
		
}
