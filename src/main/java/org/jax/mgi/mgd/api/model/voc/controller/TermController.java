package org.jax.mgi.mgd.api.model.voc.controller;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.domain.SlimTermDomain;
import org.jax.mgi.mgd.api.model.voc.domain.TermAncestorDomain;
import org.jax.mgi.mgd.api.model.voc.domain.TermDomain;
import org.jax.mgi.mgd.api.model.voc.domain.TermFamilyEdgesViewDomain;
import org.jax.mgi.mgd.api.model.voc.domain.TermFamilyViewDomain;
import org.jax.mgi.mgd.api.model.voc.service.TermService;
import org.jax.mgi.mgd.api.util.SearchResults;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/term")
@Tag(name = "Term Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TermController extends BaseController<TermDomain> {

	@Inject
	private TermService termService;

	@Override
	public SearchResults<TermDomain> create(TermDomain term, User user) {
		return termService.create(term, user);
	}

	@Override
	public SearchResults<TermDomain> update(TermDomain term, User user) {
		return termService.update(term, user);
	}

	@Override
	public TermDomain get(Integer key) {
		return termService.get(key);
	}

	@Override
	public SearchResults<TermDomain> delete(Integer key, User user) {
		return termService.delete(key, user);
	}

	@POST
	@Operation(description = "Search")
	@Path("/search")
	public List<TermDomain> search(TermDomain searchDomain) {		
		List<TermDomain> results = new ArrayList<TermDomain>();
		try {
			results = termService.search(searchDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return results;
	}

	@POST
	@Operation(description = "Validate term")
	@Path("/validateTerm")
	public List<TermDomain> validateTerm(TermDomain domain) {
		return termService.validateTerm(domain);
	}

	@POST
	@Operation(description = "Validate term slim")
	@Path("/validateTermSlim")
	public SearchResults<SlimTermDomain> validateTermSlim(SlimTermDomain domain) {
		return termService.validateTermSlim(Integer.valueOf(domain.getVocabKey()).intValue() , domain.getTerm());
	}
	
	@POST
	@Operation(description = "Validate MP header term")
	@Path("/validateMPHeaderTerm")
	public List<SlimTermDomain> validateMPHeaderTerm(SlimTermDomain domain) {
		return termService.validateMPHeaderTerm(domain);
	}
		
	@POST
	@Operation(description = "Get Terms by Set Name")
	@Path("/termset")
	public List<SlimTermDomain> getTermSet(String setName) {
		return termService.getTermSet(setName);
	}
	
	@POST
	@Operation(description = "Get list of Ancestor Terms/input string = xxx,yyy,zzz")
	@Path("/getAncestorKeys")
	public List<TermAncestorDomain> getAncestorKeys(String keys) {
		List<TermAncestorDomain> results = new ArrayList<TermAncestorDomain>();
		try {
			results = termService.getAncestorKeys(keys);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return results;	
	}

	@POST
	@Operation(description = "Get DAG Parents by term key")
	@Path("/getDagParents")
	public List<TermDomain> getDagParents(Integer termKey) {
		List<TermDomain> results = new ArrayList<TermDomain>();
		try {
			results = termService.getDagParents(termKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return results;	
	}
	
	@POST
	@Operation(description = "Get TermFamilyView by accid")
	@Path("/getTermFamilyByAccId")
	public List<TermFamilyViewDomain> getTermFamilyByAccId(String accid) {
		List<TermFamilyViewDomain> results = new ArrayList<TermFamilyViewDomain>();
		try {
			results = termService.getTermFamilyByAccId(accid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return results;	
	}	
	
	@POST
	@Operation(description = "Get TermFamilyEdgesView by accid")
	@Path("/getTermFamilyEdgesByAccId")
	public List<TermFamilyEdgesViewDomain> getTermFamilyEdgesByAccId(String accid) {
		List<TermFamilyEdgesViewDomain> results = new ArrayList<TermFamilyEdgesViewDomain>();
		try {
			results = termService.getTermFamilyEdgesByAccId(accid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return results;	
	}	
	
	@GET
	@Operation(description = "Get term by accid.")
	@Path("/getByAccid/{accid}")
	public TermDomain getByAccid(@PathParam("accid") String accid) {
	    return termService.getByAccid(accid);
	}

	@GET
	@Operation(description = "Get the immediate children of a node.")
	@Path("/getTreeViewChildren/{accid}")
	public String getTreeViewChildren(@PathParam("accid") String accid) {
	    return termService.getTreeViewChildren(accid);
	}

	@GET
	@Operation(description = "Get the tree view for the given node ID.")
	@Path("/getTreeView/{accid}")
	public String getTreeView(@PathParam("accid") String accid) {
	    return termService.getTreeView(accid);
	}
}
