package org.jax.mgi.mgd.api.model.voc.controller;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.domain.SlimTermDomain;
import org.jax.mgi.mgd.api.model.voc.domain.TermAncestorDomain;
import org.jax.mgi.mgd.api.model.voc.domain.TermDomain;
import org.jax.mgi.mgd.api.model.voc.domain.TermFamilyEdgesViewDomain;
import org.jax.mgi.mgd.api.model.voc.domain.TermFamilyViewDomain;
import org.jax.mgi.mgd.api.model.voc.service.TermService;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/term")
@Api(value = "Term Endpoints")
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
	@ApiOperation(value = "Search")
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
	@ApiOperation(value = "Validate term")
	@Path("/validateTerm")
	public List<TermDomain> validateTerm(TermDomain domain) {
		return termService.validateTerm(domain);
	}

	@POST
	@ApiOperation(value = "Validate term slim")
	@Path("/validateTermSlim")
	public SearchResults<SlimTermDomain> validateTermSlim(SlimTermDomain domain) {
		return termService.validateTermSlim(Integer.valueOf(domain.getVocabKey()).intValue() , domain.getTerm());
	}
	
	@POST
	@ApiOperation(value = "Validate MP header term")
	@Path("/validateMPHeaderTerm")
	public List<SlimTermDomain> validateMPHeaderTerm(SlimTermDomain domain) {
		return termService.validateMPHeaderTerm(domain);
	}
		
	@POST
	@ApiOperation(value = "Get Terms by Set Name")
	@Path("/termset")
	public List<SlimTermDomain> getTermSet(String setName) {
		return termService.getTermSet(setName);
	}
	
	@POST
	@ApiOperation(value = "Get list of Ancestor Terms/input string = xxx,yyy,zzz")
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
	@ApiOperation(value = "Get DAG Parents by term key")
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
	@ApiOperation(value = "Get TermFamilyView by accid")
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
	@ApiOperation(value = "Get TermFamilyEdgesView by accid")
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
	
}
