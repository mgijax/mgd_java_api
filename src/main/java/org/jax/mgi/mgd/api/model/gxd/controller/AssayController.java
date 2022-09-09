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
import org.jax.mgi.mgd.api.model.gxd.domain.AssayDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.GenotypeReplaceDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.SlimAssayDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.SlimCellTypeDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.SlimEmapaDomain;
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

	@POST
	@ApiOperation(value = "Get list of assay domains by allele accession id")
	@Path("/getAssayByAllele")
	public List<SlimAssayDomain> getAssayByAllele(String accid) {
		
		List<SlimAssayDomain> results = new ArrayList<SlimAssayDomain>();

		try {
			results = assayService.getAssayByAllele(accid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
	@POST
	@ApiOperation(value = "Get list of assay domains by marker accession id")
	@Path("/getAssayByMarker")
	public List<SlimAssayDomain> getAssayByMarker(String accid) {
		
		List<SlimAssayDomain> results = new ArrayList<SlimAssayDomain>();

		try {
			results = assayService.getAssayByMarker(accid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
	@POST
	@ApiOperation(value = "Get list of assay domains by reference jnumid")
	@Path("/getAssayByRef")
	public List<SlimAssayDomain> getAssayByRef(String jnumid) {
		
		List<SlimAssayDomain> results = new ArrayList<SlimAssayDomain>();

		try {
			results = assayService.getAssayByRef(jnumid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
	@POST
	@ApiOperation(value = "Get list of assay domains with images by reference jnumid")
	@Path("/getAssayImageByRef")
	public List<AssayDomain> getAssayImageByRef(String jnumid) {
		
		List<AssayDomain> results = new ArrayList<AssayDomain>();

		try {
			results = assayService.getAssayImageByRef(jnumid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}	

	@POST
	@ApiOperation(value = "Get list of specimen domains by reference jnumid")
	@Path("/getSpecimenByRef")
	public List<AssayDomain> getSpecimenByRef(String jnumid) {
		
		List<AssayDomain> results = new ArrayList<AssayDomain>();

		try {
			results = assayService.getSpecimenByRef(jnumid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
}
