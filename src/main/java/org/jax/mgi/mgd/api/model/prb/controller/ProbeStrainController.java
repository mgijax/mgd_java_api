package org.jax.mgi.mgd.api.model.prb.controller;

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
import org.jax.mgi.mgd.api.model.prb.domain.ProbeStrainDomain;
import org.jax.mgi.mgd.api.model.prb.domain.ProbeStrainMergeDomain;
import org.jax.mgi.mgd.api.model.prb.domain.SlimProbeStrainDomain;
import org.jax.mgi.mgd.api.model.prb.domain.StrainDataSetDomain;
import org.jax.mgi.mgd.api.model.prb.service.ProbeStrainService;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/strain")
@Api(value = "Strain Endpoints")
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
	@ApiOperation(value = "Get the object count from prb_strain table")
	@Path("/getObjectCount")
	public SearchResults<ProbeStrainDomain> getObjectCount() {
		return probeStrainService.getObjectCount();
	}
	
	@Override
	public SearchResults<ProbeStrainDomain> delete(Integer key, User user) {
		return probeStrainService.delete(key, user);
	}

	@POST
	@ApiOperation(value = "Search/returns slim probe strain domain")
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
	@ApiOperation(value = "Validate Strain")
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
	
	@GET
	@ApiOperation(value = "get list of strains")
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
	@ApiOperation(value = "get list of strains for probes/antigen")
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
	@ApiOperation(value = "Get accession data sets by strain key")
	@Path("/getDataSetsAcc/{key}")
	public List<StrainDataSetDomain> getDataSetsAcc(@PathParam("key") Integer key) {
		return probeStrainService.getDataSetsAcc(key);
	}

	@GET
	@ApiOperation(value = "Get reference data sets by strain key")
	@Path("/getDataSetsRef/{key}")
	public List<StrainDataSetDomain> getDataSetsRef(@PathParam("key") Integer key) {
		return probeStrainService.getDataSetsRef(key);
	}

	@GET
	@ApiOperation(value = "Get strains by refs key")
	@Path("/getByRef/{key}")
	public List<SlimProbeStrainDomain> getByRef(@PathParam("key") Integer key) {
		return probeStrainService.getByRef(key);
	}

	@POST
	@ApiOperation(value = "Process Strain Merge/returns slim probe strain domain")
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
	@ApiOperation(value = "Search Duplicates/returns slim probe strain domain")
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
	
}
