package org.jax.mgi.mgd.api.model.prb.controller;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.gxd.domain.SlimGenotypeDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.prb.domain.ProbeStrainDomain;
import org.jax.mgi.mgd.api.model.prb.domain.ProbeStrainMergeDomain;
import org.jax.mgi.mgd.api.model.prb.domain.SlimProbeStrainDomain;
import org.jax.mgi.mgd.api.model.prb.domain.SlimProbeStrainToolDomain;
import org.jax.mgi.mgd.api.model.prb.domain.StrainDataSetDomain;
import org.jax.mgi.mgd.api.model.prb.service.ProbeStrainService;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SearchResults;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/strain")
@Tag(name = "Strain Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProbeStrainController extends BaseController<ProbeStrainDomain> {

	@Inject
	private ProbeStrainService probeStrainService;

	@Override
	public SearchResults<ProbeStrainDomain> create(ProbeStrainDomain domain, User user) {
		SearchResults<ProbeStrainDomain> results = new SearchResults<ProbeStrainDomain>();
		results = probeStrainService.create(domain, user);
		results = probeStrainService.getResults(Integer.valueOf(results.items.get(0).getStrainKey()));
		return results;
	}

	@Override
	public SearchResults<ProbeStrainDomain> update(ProbeStrainDomain domain, User user) {
		SearchResults<ProbeStrainDomain> results = new SearchResults<ProbeStrainDomain>();
		results = probeStrainService.update(domain, user);
		results = probeStrainService.getResults(Integer.valueOf(results.items.get(0).getStrainKey()));
		return results;
	}

	@Override
	public ProbeStrainDomain get(Integer key) {
		return probeStrainService.get(key);
	}

	@GET
	@Operation(description = "Get the object count from prb_strain table")
	@Path("/getObjectCount")
	public SearchResults<ProbeStrainDomain> getObjectCount() {
		return probeStrainService.getObjectCount();
	}
	
	@Override
	public SearchResults<ProbeStrainDomain> delete(Integer key, User user) {
		return probeStrainService.delete(key, user);
	}

	@POST
	@Operation(description = "Search/returns slim probe strain domain")
	@Path("/search")
	public List<SlimProbeStrainDomain> search(ProbeStrainDomain searchDomain) {
	
		List<SlimProbeStrainDomain> results = new ArrayList<SlimProbeStrainDomain>();

		try {
			results = probeStrainService.search(searchDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
	@POST
	@Operation(description = "Search/returns slim probe strain tool domain")
	@Path("/searchStrainTool")
	public List<SlimProbeStrainToolDomain> searchStrainTool(SlimProbeStrainToolDomain searchDomain) {
	
		List<SlimProbeStrainToolDomain> results = new ArrayList<SlimProbeStrainToolDomain>();

		try {
			results = probeStrainService.searchStrainTool(searchDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
	@POST
	@Operation(description = "Validate Strain")
	@Path("/validateStrain")
	public List<SlimProbeStrainDomain> validateStrain(SlimProbeStrainDomain searchDomain) {
		
		List<SlimProbeStrainDomain> results = new ArrayList<SlimProbeStrainDomain>();

		try {
			results = probeStrainService.validateStrain(searchDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
	@POST
	@Operation(description = "Validate Strain/Private can be changed")
	@Path("/validateStrainPrivate")
	public List<SlimProbeStrainDomain> validateStrainPrivate(SlimProbeStrainDomain searchDomain) {
		
		List<SlimProbeStrainDomain> results = new ArrayList<SlimProbeStrainDomain>();

		try {
			results = probeStrainService.validateStrainPrivate(searchDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
	@GET
	@Operation(description = "get list of strains")
	@Path("/getStrainList")
	public SearchResults<String> getStrainList() {
	
		SearchResults<String> results = null;

		try {
			results = probeStrainService.getStrainList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//log.info(results);
		return results;
	}

	@GET
	@Operation(description = "get list of strains for probes/antigen")
	@Path("/getStrainListProbeAntigen")
	public SearchResults<String> getStrainListProbeAntigen() {
	
		SearchResults<String> results = null;

		try {
			results = probeStrainService.getStrainListProbeAntigen();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//log.info(results);
		return results;
	}

	@GET
	@Operation(description = "Get accession data sets by strain key")
	@Path("/getDataSetsAcc/{key}")
	public List<StrainDataSetDomain> getDataSetsAcc(@PathParam("key") Integer key) {
		return probeStrainService.getDataSetsAcc(key);
	}

	@GET
	@Operation(description = "Get reference data sets by strain key")
	@Path("/getDataSetsRef/{key}")
	public List<StrainDataSetDomain> getDataSetsRef(@PathParam("key") Integer key) {
		return probeStrainService.getDataSetsRef(key);
	}

	@GET
	@Operation(description = "Get strains by refs key")
	@Path("/getByRef/{key}")
	public List<SlimProbeStrainDomain> getByRef(@PathParam("key") Integer key) {
		return probeStrainService.getByRef(key);
	}

	@POST
	@Operation(description = "Process Strain Merge/returns slim probe strain domain")
	@Path("/processMerge")
	public SearchResults<SlimProbeStrainDomain> processMerge(ProbeStrainMergeDomain mergeDomain) {
	
		SearchResults<SlimProbeStrainDomain> results = new SearchResults<SlimProbeStrainDomain>();

		try {
			results = probeStrainService.processMerge(mergeDomain);
		} catch (Exception e) {
			Throwable t = getRootException(e);
			StackTraceElement[] ste = t.getStackTrace();
			String message = t.toString() + " [" + ste[0].getFileName() + ":" + ste[0].getLineNumber() + "]" + " (" + t.getMessage() + ")";
			results.setError(Constants.LOG_FAIL_DOMAIN, message, Constants.HTTP_SERVER_ERROR);				
		}
		
		if (results.error == null || results.error.isEmpty()) {
			try {
				results = probeStrainService.searchMergeResults(mergeDomain);
			} catch (Exception e) {
				e.printStackTrace();		
			}
		}
		
		return results;
	}

	@POST
	@Operation(description = "Search Duplicates/returns slim probe strain domain")
	@Path("/searchDuplicates")
	public List<SlimProbeStrainDomain> searchDuplicates() {
	
		List<SlimProbeStrainDomain> results = new ArrayList<SlimProbeStrainDomain>();

		try {
			results = probeStrainService.searchDuplicates();
		} catch (Exception e) {
			e.printStackTrace();		
		}
		
		return results;
	}

	@POST
	@Operation(description = "Validate Genotype/Strain association exists, returns List of SlimGenotypeDomain")
	@Path("/validateGenotypeStrain")
	public List<SlimGenotypeDomain> validateGenotypeStrain(SlimProbeStrainDomain searchDomain) {
	
		List<SlimGenotypeDomain> results = new ArrayList<SlimGenotypeDomain>();

		try {
			results = probeStrainService.validateGenotypeStrain(searchDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}	
	
}
