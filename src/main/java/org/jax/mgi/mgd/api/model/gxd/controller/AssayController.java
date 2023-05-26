package org.jax.mgi.mgd.api.model.gxd.controller;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.gxd.domain.AssayDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.GenotypeReplaceDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.SlimAssayDLDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.SlimAssayDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.SlimCellTypeDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.SlimEmapaDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.SummaryResultDomain;
import org.jax.mgi.mgd.api.model.gxd.service.AssayService;
import org.jax.mgi.mgd.api.model.mgi.domain.MGISetMemberCellTypeDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.MGISetMemberEmapaDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.MGISetMemberGenotypeDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/assay")
@Api(value = "Assay Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AssayController extends BaseController<AssayDomain> {

	@Inject
	private AssayService assayService;

	@Override
	public SearchResults<AssayDomain> create(AssayDomain domain, User user) {
		SearchResults<AssayDomain> results = new SearchResults<AssayDomain>();
		results = assayService.create(domain, user);
		
		// to update the mgicacheload/gxdexpression table				
		try {
			log.info("processAssay/gxdexpressionUtilities");
			assayService.gxdexpressionUtilities(results.items.get(0).getAssayKey());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
				
		results = assayService.getResults(Integer.valueOf(results.items.get(0).getAssayKey()));
		return results;
	}

	@Override
	public SearchResults<AssayDomain> update(AssayDomain domain, User user) {
		SearchResults<AssayDomain> results = new SearchResults<AssayDomain>();
		results = assayService.update(domain, user);
		
		// to update the mgicacheload/gxdexpression table				
		try {
			log.info("processAssay/gxdexpressionUtilities");
			assayService.gxdexpressionUtilities(results.items.get(0).getAssayKey());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		results = assayService.getResults(Integer.valueOf(results.items.get(0).getAssayKey()));
		return results;
	}

	@Override
	public SearchResults<AssayDomain> delete(Integer key, User user) {
		return assayService.delete(key, user);
	}
	
	@Override
	public AssayDomain get(Integer key) {
		return assayService.get(key);
	}

	@GET
	@ApiOperation(value = "Get the object count from gxd_assay table")
	@Path("/getObjectCount")
	public SearchResults<AssayDomain> getObjectCount() {
		return assayService.getObjectCount();
	}
		
	@POST
	@ApiOperation(value = "Search/returns Assay domain")
	@Path("/search")
	public List<SlimAssayDomain> search(AssayDomain searchDomain) {
	
		List<SlimAssayDomain> results = new ArrayList<SlimAssayDomain>();

		try {
			results = assayService.search(searchDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
	@POST
	@ApiOperation(value = "Get Genotype Set Members by Specimen and Set/User")
	@Path("/getGenotypeBySetUser")	
	public List<MGISetMemberGenotypeDomain> getGenotypeBySetUser(SlimAssayDomain domain) {
			
		List<MGISetMemberGenotypeDomain> results = new ArrayList<MGISetMemberGenotypeDomain>();
		
		try {
			results = assayService.getGenotypeBySetUser(domain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
	@POST
	@ApiOperation(value = "Get EMAPA Set Members by Specimen and Set/User")
	@Path("/getEmapaInSituBySetUser")
	public List<MGISetMemberEmapaDomain> getEmapaBySetUser(SlimEmapaDomain domain) {
			
		List<MGISetMemberEmapaDomain> results = new ArrayList<MGISetMemberEmapaDomain>();
		
		try {
			results = assayService.getEmapaInSituBySetUser(domain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}

	@POST
	@ApiOperation(value = "Get EMAPA Set Members by Specimen and Set/User")
	@Path("/getEmapaGelBySetUser")
	public List<MGISetMemberEmapaDomain> getEmapaGelBySetUser(SlimEmapaDomain domain) {
			
		List<MGISetMemberEmapaDomain> results = new ArrayList<MGISetMemberEmapaDomain>();
		
		try {
			results = assayService.getEmapaGelBySetUser(domain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}

	@POST
	@ApiOperation(value = "Get CellType Set Members by Specimen and Set/User")
	@Path("/getCellTypeInSituBySetUser")
	public List<MGISetMemberCellTypeDomain> getCellTypeBySetUser(SlimCellTypeDomain domain) {
			
		List<MGISetMemberCellTypeDomain> results = new ArrayList<MGISetMemberCellTypeDomain>();
		
		try {
			results = assayService.getCellTypeInSituBySetUser(domain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
	@POST
	@ApiOperation(value = "Add to EMAPA clipboard")
	@Path("/addToEmapaClipboard")
	public List<SlimAssayDomain> addToEmapaClipboard(SlimAssayDomain domain) {
	
		List<SlimAssayDomain> results = new ArrayList<SlimAssayDomain>();		

		try {
			results = assayService.addToEmapaClipboard(domain);
		} catch (Exception e) {
			e.printStackTrace();				
		}
		
		return results;
	}

	@POST
	@ApiOperation(value = "Add to Cell Type clipboard")
	@Path("/addToCellTypeClipboard")
	public List<SlimAssayDomain> addToCellTypeClipboard(SlimAssayDomain domain) {
	
		List<SlimAssayDomain> results = new ArrayList<SlimAssayDomain>();		

		try {
			results = assayService.addToCellTypeClipboard(domain);
		} catch (Exception e) {
			e.printStackTrace();				
		}
		
		return results;
	}
	
	@POST
	@ApiOperation(value = "Add to Genotype clipboard")
	@Path("/addToGenotypeClipboard")
	public List<SlimAssayDomain> addToGenotypeClipboard(SlimAssayDomain domain) {
	
		List<SlimAssayDomain> results = new ArrayList<SlimAssayDomain>();		

		try {
			results = assayService.addToGenotypeClipboard(domain);
		} catch (Exception e) {
			e.printStackTrace();				
		}
		
		return results;
	}
	
	@POST
	@ApiOperation(value = "Process Replace Genotype/returns GenotypeReplaceDomain")
	@Path("/processReplaceGenotype")
	public List<GenotypeReplaceDomain> processReplaceGenotype(GenotypeReplaceDomain domain) {
	
		List<GenotypeReplaceDomain> results = new ArrayList<GenotypeReplaceDomain>();		

		try {
			results = assayService.processReplaceGenotype(domain);
		} catch (Exception e) {
			e.printStackTrace();				
		}
		
		return results;
	}

	// -----------------------------------------------------
	// get assay by allele

	@GET
	@ApiOperation(value = "Get list of assay domains by allele accession id")
	@Path("/getAssayByAllele")
	public List<SlimAssayDomain> getAssayByAllele(@QueryParam("accid") String accid) {
		
		List<SlimAssayDomain> results = new ArrayList<SlimAssayDomain>();

		try {
			results = assayService.getAssayByAllele(accid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return results;
	}
	
	@GET
	@ApiOperation(value = "Download TSV file.")
	@Path("/downloadAssayByAllele")
        @Produces(MediaType.TEXT_PLAIN)
	public Response downloadAssayByAllele(@QueryParam("accid") String accid) {
		return assayService.downloadAssayByAllele(accid);
	}
	
	// -----------------------------------------------------
	// get assay by antibody

	@GET
	@ApiOperation(value = "Get list of assay domains by antibody accession id")
	@Path("/getAssayByAntibody")
	public List<SlimAssayDomain> getAssayByAntibody(@QueryParam("accid") String accid) {
		
		List<SlimAssayDomain> results = new ArrayList<SlimAssayDomain>();

		try {
			results = assayService.getAssayByAntibody(accid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}	
	
	@GET
	@ApiOperation(value = "Download TSV file.")
	@Path("/downloadAssayByAntibody")
        @Produces(MediaType.TEXT_PLAIN)
	public Response downloadAssayByAntibody(@QueryParam("accid") String accid) {
		return assayService.downloadAssayByAntibody(accid);
	}
	
	// -----------------------------------------------------
	// get assay by marker

	@GET
	@ApiOperation(value = "Get list of assay domains by marker accession id")
	@Path("/getAssayByMarker")
	public List<SlimAssayDomain> getAssayByMarker(@QueryParam("accid") String accid) {
		
		List<SlimAssayDomain> results = new ArrayList<SlimAssayDomain>();

		try {
			results = assayService.getAssayByMarker(accid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}	
	
	@GET
	@ApiOperation(value = "Download TSV file.")
	@Path("/downloadAssayByMarker")
        @Produces(MediaType.TEXT_PLAIN)
	public Response downloadAssayByMarker(@QueryParam("accid") String accid) {
		return assayService.downloadAssayByMarker(accid);
	}

	// -----------------------------------------------------
	// get assay by probe

	@GET
	@ApiOperation(value = "Get list of assay domains by probe accession id")
	@Path("/getAssayByProbe")
	public List<SlimAssayDomain> getAssayByProbe(@QueryParam("accid") String accid) {
		
		List<SlimAssayDomain> results = new ArrayList<SlimAssayDomain>();

		try {
			results = assayService.getAssayByProbe(accid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
	@GET
	@ApiOperation(value = "Download TSV file.")
	@Path("/downloadAssayByProbe")
        @Produces(MediaType.TEXT_PLAIN)
	public Response downloadAssayByProbe(@QueryParam("accid") String accid) {
		return assayService.downloadAssayByProbe(accid);
	}

	// -----------------------------------------------------
	// get assay by probe

	@GET
	@ApiOperation(value = "Get list of assay domains by jnumid")
	@Path("/getAssayByRef")
	public List<SlimAssayDomain> getAssayByRef(@QueryParam("accid") String jnumid) {
		
		List<SlimAssayDomain> results = new ArrayList<SlimAssayDomain>();

		try {
			results = assayService.getAssayByRef(jnumid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
	@GET
	@ApiOperation(value = "Download TSV file.")
	@Path("/downloadAssayByRef")
        @Produces(MediaType.TEXT_PLAIN)
	public Response downloadAssayByRef(@QueryParam("accid") String accid) {
		return assayService.downloadAssayByRef(accid);
	}

	// -----------------------------------------------------
	// get result by cell type

	@GET
	@ApiOperation(value = "Get list of summary result domains by cell type")
	@Path("/getResultByCellType")
	public SearchResults<SummaryResultDomain> getResultByCellType(
                @QueryParam("accid") String accid,
                @QueryParam("offset") int offset,
                @QueryParam("limit") int limit
        ) {
		
		SearchResults<SummaryResultDomain> results = new SearchResults<SummaryResultDomain>();

		try {
			results = assayService.getResultByCellType(accid, offset, limit);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}	

	@GET
	@ApiOperation(value = "Download TSV file.")
	@Path("/downloadResultByCellType")
        @Produces(MediaType.TEXT_PLAIN)
	public Response downloadResultByCellType(@QueryParam("accid") String accid) {
             return assayService.downloadResultByCellType(accid);
	}
	
	
	// -----------------------------------------------------
	// get result by marker

	@GET
	@ApiOperation(value = "Get list of summary result domains by markerid")
	@Path("/getResultByMarker")
	public SearchResults<SummaryResultDomain> getResultByMarker(
                @QueryParam("accid") String accid,
                @QueryParam("offset") int offset,
                @QueryParam("limit") int limit
        ) {
		
		SearchResults<SummaryResultDomain> results = new SearchResults<SummaryResultDomain>();

		try {
			results = assayService.getResultByMarker(accid, offset, limit);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
	@GET
	@ApiOperation(value = "Download TSV file.")
	@Path("/downloadResultByMarker")
        @Produces(MediaType.TEXT_PLAIN)
	public Response downloadResultByMarker(@QueryParam("accid") String accid) {
             return assayService.downloadResultByMarker(accid);
	}
	
	// -----------------------------------------------------
	// get result by reference

	@GET
	@ApiOperation(value = "Get list of summary result domains by reference jnumid")
	@Path("/getResultByRef")
	public SearchResults<SummaryResultDomain> getResultByRef(
                @QueryParam("accid") String accid,
                @QueryParam("offset") int offset,
                @QueryParam("limit") int limit
        ) {
		SearchResults<SummaryResultDomain> results = new SearchResults<SummaryResultDomain>();

		try {
			results = assayService.getResultByRef(accid, offset, limit);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}

	@GET
	@ApiOperation(value = "Download TSV file.")
	@Path("/downloadResultByRef")
        @Produces(MediaType.TEXT_PLAIN)
	public Response downloadResultByRef(@QueryParam("accid") String accid) {
             return assayService.downloadResultByRef(accid);
	}
	
	// -----------------------------------------------------
	// get result by structure

	@GET
	@ApiOperation(value = "Get list of summary result domains by structure")
	@Path("/getResultByStructure")
	public SearchResults<SummaryResultDomain> getResultByStructure(
                @QueryParam("accid") String accid,
                @QueryParam("offset") int offset,
                @QueryParam("limit") int limit
        ) {
		
		SearchResults<SummaryResultDomain> results = new SearchResults<SummaryResultDomain>();

		try {
			results = assayService.getResultByStructure(accid, offset, limit);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}	
	
	@GET
	@ApiOperation(value = "Download TSV file.")
	@Path("/downloadResultByStructure")
        @Produces(MediaType.TEXT_PLAIN)
	public Response downloadResultByStructure(@QueryParam("accid") String accid) {
             return assayService.downloadResultByStructure(accid);
	}
	
	@POST
	@ApiOperation(value = "Get Assay Double Label (DL) by assay key")
	@Path("/getAssayDLByKey")	
	public List<SlimAssayDLDomain> getAssayDLByKey(String assayKey) {
		
		List<SlimAssayDLDomain> results = new ArrayList<SlimAssayDLDomain>();

		try {
			results = assayService.getAssayDLByKey(assayKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}	
}
