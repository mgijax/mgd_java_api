package org.jax.mgi.mgd.api.model.gxd.controller;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.gxd.domain.GenotypeDataSetDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.GenotypeDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.SlimGenotypeDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.SummaryGenotypeDomain;
import org.jax.mgi.mgd.api.model.gxd.service.AllelePairService;
import org.jax.mgi.mgd.api.model.gxd.service.GenotypeService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/genotype")
@Tag(name = "Genotype Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GenotypeController extends BaseController<GenotypeDomain> {

	protected Logger log = Logger.getLogger(getClass());
	ObjectMapper mapper = new ObjectMapper();
	
	@Inject
	private GenotypeService genotypeService;
	@Inject
	private AllelePairService allelePairService;
	
	// refresh/resync the results due to database triggers
	// for example, the mgi accession id is created by a database trigger
	
	@Override
	public SearchResults<GenotypeDomain> create(GenotypeDomain domain, User user) {	
		SearchResults<GenotypeDomain> results = new SearchResults<GenotypeDomain>();
		results = genotypeService.create(domain, user);

		// set the allele-combination notes
		log.info("processGenotype/alleleCombinationUtilities");	
		try {
			if (allelePairService.alleleCombinationUtilities(results.items.get(0).getGenotypeKey()) == true) {
				log.info("processGenotype/alleleCombinationUtilities: successful");	
			}
			else {
				log.info("processGenotype/alleleCombinationUtilities: failure");	
			}				
		} catch (Exception e) {
			results.setError(Constants.LOG_FAIL_EIUTILITIES, e.getMessage(), Constants.HTTP_SERVER_ERROR);
		}
		
		results = genotypeService.getResults(Integer.valueOf(results.items.get(0).getGenotypeKey()));
		return results;
	}

	@Override
	public SearchResults<GenotypeDomain> update(GenotypeDomain domain, User user) {
		SearchResults<GenotypeDomain> results = new SearchResults<GenotypeDomain>();
		results = genotypeService.update(domain, user);

		// set the allele-combination notes
		log.info("processGenotype/alleleCombinationUtilities");	
		try {
			if (allelePairService.alleleCombinationUtilities(results.items.get(0).getGenotypeKey()) == true) {
				log.info("processGenotype/alleleCombinationUtilities: successful");	
			}
			else {
				log.info("processGenotype/alleleCombinationUtilities: failure");	
			}				
		} catch (Exception e) {
			results.setError(Constants.LOG_FAIL_EIUTILITIES, e.getMessage(), Constants.HTTP_SERVER_ERROR);
		}
				
		results = genotypeService.getResults(Integer.valueOf(results.items.get(0).getGenotypeKey()));
		return results;	
	}

	@Override
	public GenotypeDomain get(Integer genotypeKey) {
		return genotypeService.get(genotypeKey);
	}

	@Override
	public SearchResults<GenotypeDomain> delete(Integer key, User user) {
		return genotypeService.delete(key, user);
	}

	@GET
	@Operation(description = "Get the object count from gxd_genotype table")
	@Path("/getObjectCount")
	public SearchResults<GenotypeDomain> getObjectCount() {
		return genotypeService.getObjectCount();
	}
		
	@POST
	@Operation(description = "Search/returns slim genotype domain")
	@Path("/search")
	public List<SlimGenotypeDomain> search(GenotypeDomain searchDomain) {
	
		List<SlimGenotypeDomain> results = new ArrayList<SlimGenotypeDomain>();

		try {
			results = genotypeService.search(searchDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
	@GET
	@Operation(description = "Get genotype data sets by genotype key")
	@Path("/getDataSets/{key}")
	public List<GenotypeDataSetDomain> getDataSet(@PathParam("key") Integer key) {
		return genotypeService.getDataSets(key);
	}
	
	@GET
	@Operation(description = "Search genotypes by jnum key")
	@Path("/searchDataSets/{key}")
	public List<SlimGenotypeDomain> searchDataSet(@PathParam("key") Integer key) {
		return genotypeService.searchDataSets(key);
	}

	@POST
	@Operation(description = "Validate Genotype exists, returns List of SlimGenotypeDomain")
	@Path("/validateGenotype")
	public List<SlimGenotypeDomain> validateGenotype(SlimGenotypeDomain searchDomain) {
	
		List<SlimGenotypeDomain> results = new ArrayList<SlimGenotypeDomain>();

		try {
			results = genotypeService.validateGenotype(searchDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
	@POST
	@Operation(description = "Get list of genotype domains by allele key")
	@Path("/getGenotypesByAllele")
	public List<GenotypeDomain> getGenotypesByAllele(String key) {
		
		List<GenotypeDomain> results = new ArrayList<GenotypeDomain>();

		try {
			results = genotypeService.getGenotypesByAllele(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}

	@GET
	@Operation(description = "Get list of genotype domains by reference jnum id")
	@Path("/getGenotypeByRef")
	public SearchResults<SummaryGenotypeDomain> getGenotypeByRef(
		@QueryParam("accid") String accid,
		@QueryParam("offset") int offset,
		@QueryParam("limit") int limit
		) {

		SearchResults<SummaryGenotypeDomain> results = new SearchResults<SummaryGenotypeDomain>();

		try {
			results = genotypeService.getGenotypeByRef(accid, offset, limit);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
	@GET
	@Operation(description = "Download TSV file.")
	@Path("/downloadGenotypeByRef")
	@Produces(MediaType.TEXT_PLAIN)
	public Response downloadGenotypeByRef(@QueryParam("accid") String accid) {
		return genotypeService.downloadGenotypeByJnum(accid);
	}

	@POST
	@Operation(description = "Get list of genotype domains by acc ids")
	@Path("/getGenotypeByAccIDs")
	public SearchResults<SummaryGenotypeDomain> getGenotypeByAccIDs(String accid) {

		SearchResults<SummaryGenotypeDomain> results = new SearchResults<SummaryGenotypeDomain>();

		try {
			results = genotypeService.getGenotypeByAccIDs(accid, 0, 100000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
	@GET
	@Operation(description = "Download TSV file.")
	@Path("/downloadGenotypeByAccIDs")
	@Produces(MediaType.TEXT_PLAIN)
	public Response downloadGenotypeByAccIDs(@QueryParam("accid") String accid) {
		return genotypeService.downloadGenotypeByAccIDs(accid);
	}
	
	@GET
	@Operation(description = "Get list of genotype domains by clipboard userid")
	@Path("/getGenotypeByClipboard")
	public SearchResults<SummaryGenotypeDomain> getGenotypeByClipboard(
		@QueryParam("accid") String userid,
		@QueryParam("offset") int offset,
		@QueryParam("limit") int limit
		) {

		SearchResults<SummaryGenotypeDomain> results = new SearchResults<SummaryGenotypeDomain>();

		try {
			results = genotypeService.getGenotypeByClipboard(userid, offset, limit);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
	@GET
	@Operation(description = "Download TSV file.")
	@Path("/downloadGenotypeByClipboard")
	@Produces(MediaType.TEXT_PLAIN)
	public Response downloadGenotypeByClipboard(@QueryParam("accid") String userid) {
		return genotypeService.downloadGenotypeByClipboard(userid);
	}
	
}
