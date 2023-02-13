package org.jax.mgi.mgd.api.model.prb.controller;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.prb.domain.ProbeDomain;
import org.jax.mgi.mgd.api.model.prb.domain.SlimProbeDomain;
import org.jax.mgi.mgd.api.model.prb.domain.SlimProbeSummaryDomain;
import org.jax.mgi.mgd.api.model.prb.domain.SummaryProbeDomain;
import org.jax.mgi.mgd.api.model.prb.service.ProbeService;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/probe")
@Api(value = "Probe Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProbeController extends BaseController<ProbeDomain> {

	@Inject
	private ProbeService probeService;

	@Override
	public SearchResults<ProbeDomain> create(ProbeDomain domain, User user) {
		SearchResults<ProbeDomain> results = new SearchResults<ProbeDomain>();
		results = probeService.create(domain, user);
		results = probeService.getResults(Integer.valueOf(results.items.get(0).getProbeKey()));
		return results;
	}

	@Override
	public SearchResults<ProbeDomain> update(ProbeDomain domain, User user) {
		SearchResults<ProbeDomain> results = new SearchResults<ProbeDomain>();
		results = probeService.update(domain, user);
		results = probeService.getResults(Integer.valueOf(results.items.get(0).getProbeKey()));
		return results;
	}

	@Override
	public SearchResults<ProbeDomain> delete(Integer key, User user) {
		return probeService.delete(key, user);
	}
	
	@Override
	public ProbeDomain get(Integer key) {
		return probeService.get(key);
	}

	@GET
	@ApiOperation(value = "Get the object count from prb_probe table")
	@Path("/getObjectCount")
	public SearchResults<ProbeDomain> getObjectCount() {
		return probeService.getObjectCount();
	}
		
	@POST
	@ApiOperation(value = "Search/returns slim probe domain")
	@Path("/search")
	public List<SlimProbeDomain> search(ProbeDomain searchDomain) {
	
		List<SlimProbeDomain> results = new ArrayList<SlimProbeDomain>();

		try {
			results = probeService.search(searchDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}

	@POST
	@ApiOperation(value = "Validate Probe by accID, returns List of SlimProbeDomain")
	@Path("/validateProbe")
	public List<SlimProbeDomain> validateProbe(SlimProbeDomain searchDomain) {
	
		List<SlimProbeDomain> results = new ArrayList<SlimProbeDomain>();

		try {
			results = probeService.validateProbe(searchDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}

	@POST
	@ApiOperation(value = "Validate Amp Primer by accID, returns List of SlimProbeDomain")
	@Path("/validateAmpPrimer")
	public List<SlimProbeDomain> validateAmpPrimer(SlimProbeDomain searchDomain) {
	
		List<SlimProbeDomain> results = new ArrayList<SlimProbeDomain>();

		try {
			results = probeService.validateAmpPrimer(searchDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
	@GET
	@ApiOperation(value = "Get list of summary domains by marker accession id")
	@Path("/getProbeByMarker")
	public SearchResults<SummaryProbeDomain> getProbeByMarker(
		@QueryParam("accid") String accid,
		@QueryParam("offset") int offset,
		@QueryParam("limit") int limit
		) {
		
		SearchResults<SummaryProbeDomain> results = new SearchResults<SummaryProbeDomain>();

		try {
			results = probeService.getProbeByMarker(accid, offset, limit);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
	@GET
	@ApiOperation(value = "Download TSV file.")
	@Path("/downloadProbeByMarker")
        @Produces(MediaType.TEXT_PLAIN)
	public Response downloadProbeByMarker(@QueryParam("accid") String accid) {
             return probeService.downloadProbeByMarker(accid);
	}
	
	@GET
	@ApiOperation(value = "Get list of summary domains by reference jnumid")
	@Path("/getProbeByRef")
	public SearchResults<SummaryProbeDomain> getProbeByRef(
		@QueryParam("accid") String accid,
		@QueryParam("offset") int offset,
		@QueryParam("limit") int limit
		) {
		
		SearchResults<SummaryProbeDomain> results = new SearchResults<SummaryProbeDomain>();

		try {
			results = probeService.getProbeByRef(accid,offset,limit);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
	@GET
	@ApiOperation(value = "Download TSV file.")
	@Path("/downloadProbeByRef")
        @Produces(MediaType.TEXT_PLAIN)
	public Response downloadProbeByRef(@QueryParam("accid") String accid) {
             return probeService.downloadProbeByRef(accid);
	}
	
	@GET
	@ApiOperation(value = "Get list of summary domains by search")
	@Path("/getProbeBySearch")
	public SearchResults<SummaryProbeDomain> getProbeBySearch(
		@QueryParam("name") String name,
		@QueryParam("segmentTypeKey") String segmentTypeKey,
		@QueryParam("offset") int offset,
		@QueryParam("limit") int limit
		) {
		
		SearchResults<SummaryProbeDomain> results = new SearchResults<SummaryProbeDomain>();

		try {
			results = probeService.getProbeBySearch(name, segmentTypeKey, offset, limit);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}	
	
	@GET
	@ApiOperation(value = "Download TSV file.")
	@Path("/downloadProbeBySearch")
        @Produces(MediaType.TEXT_PLAIN)
	public Response downloadProbeBySearch(
		@QueryParam("name") String name,
		@QueryParam("segmentTypeKey") String segmentTypeKey
	) {
             return probeService.downloadProbeBySearch(name, segmentTypeKey);
	}
	@POST
	@ApiOperation(value = "Get list of child clones of probe key")
	@Path("/getChildClones")
	public List<SlimProbeSummaryDomain> getChildClones(Integer key) {
		
		List<SlimProbeSummaryDomain> results = new ArrayList<SlimProbeSummaryDomain>();

		try {
			results = probeService.getChildClones(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}	
}
