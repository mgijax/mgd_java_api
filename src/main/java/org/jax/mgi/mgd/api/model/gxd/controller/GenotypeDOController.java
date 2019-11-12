package org.jax.mgi.mgd.api.model.gxd.controller;

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
import org.jax.mgi.mgd.api.model.gxd.domain.DenormGenotypeAnnotDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.SlimGenotypeAlleleReferenceDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.SlimGenotypeDomain;
import org.jax.mgi.mgd.api.model.gxd.service.GenotypeAnnotService;
import org.jax.mgi.mgd.api.model.mgi.domain.MGIReferenceAssocDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/genotypeDOannot")
@Api(value = "Genotype DO Annotations Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GenotypeDOController extends BaseController<DenormGenotypeAnnotDomain> {

	protected Logger log = Logger.getLogger(getClass());
	ObjectMapper mapper = new ObjectMapper();
	String annotType = "1020";
	
	@Inject
	private GenotypeAnnotService genotypeAnnotService;

	// refresh/resync the results due to database triggers
	// for example, the mgi accession id is created by a database trigger
	
	@Override
	public SearchResults<DenormGenotypeAnnotDomain> create(DenormGenotypeAnnotDomain domain, User user) {	
		log.info("GenotypeDOController.create");
		SearchResults<DenormGenotypeAnnotDomain> results = new SearchResults<DenormGenotypeAnnotDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);	
		return results;
	}

	@Override
	public SearchResults<DenormGenotypeAnnotDomain> update(DenormGenotypeAnnotDomain domain, User user) {
		SearchResults<DenormGenotypeAnnotDomain> results = new SearchResults<DenormGenotypeAnnotDomain>();
		results = genotypeAnnotService.update(domain, user);
		
		return results;	
	}

	@Override
	public DenormGenotypeAnnotDomain get(Integer genotypeKey) {
		return genotypeAnnotService.get(genotypeKey, Integer.valueOf(annotType));
	}

	@Override
	public SearchResults<DenormGenotypeAnnotDomain> delete(Integer key, User user) {
		log.info("GenotypeDOController.delete");
		SearchResults<DenormGenotypeAnnotDomain> results = new SearchResults<DenormGenotypeAnnotDomain>();
		results.setError(Constants.LOG_NOT_IMPLEMENTED, null, Constants.HTTP_SERVER_ERROR);
		return results;		
	}

	@GET
	@ApiOperation(value = "Get the object count from voc_annot table where _annottype_key = 1020")
	@Path("/getObjectCount")
	public SearchResults<DenormGenotypeAnnotDomain> getObjectCount() {
		return genotypeAnnotService.getObjectCount(annotType);
	}
		
	@POST
	@ApiOperation(value = "Search/returns slim genotype domain")
	@Path("/search")
	public List<SlimGenotypeDomain> search(DenormGenotypeAnnotDomain searchDomain) {
	
		List<SlimGenotypeDomain> results = new ArrayList<SlimGenotypeDomain>();

		try {
			results = genotypeAnnotService.search(searchDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}

	@POST
	@ApiOperation(value = "Validate Allele-Reference associations for Genotype")
	@Path("/validateAlleleReference")
	public List<MGIReferenceAssocDomain> validateAlleleReference(SlimGenotypeAlleleReferenceDomain searchDomain) {
		
		List<MGIReferenceAssocDomain> results = new ArrayList<MGIReferenceAssocDomain>();

		try {
			results = genotypeAnnotService.validateAlleleReference(searchDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
		
}
