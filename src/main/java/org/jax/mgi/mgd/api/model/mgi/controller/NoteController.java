package org.jax.mgi.mgd.api.model.mgi.controller;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.mgi.domain.NoteDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mgi.service.NoteService;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/note")
@Api(value = "Note Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NoteController extends BaseController<NoteDomain> {

	@Inject
	private NoteService noteService;

	@Override
	public SearchResults<NoteDomain> create(NoteDomain note, User user) {
		return noteService.create(note, user);
	}

	@Override
	public SearchResults<NoteDomain> update(NoteDomain note, User user) {
		return noteService.update(note, user);
	}

	@Override
	public NoteDomain get(Integer key) {
		return noteService.get(key);
	}

	@Override
	public SearchResults<NoteDomain> delete(Integer key, User user) {
		return noteService.delete(key, user);
	}

	@POST
	@ApiOperation(value = "Get All Notes by Marker")
	@Path("/marker")
	public List<NoteDomain> getByMarker(Integer key) {
			
		List<NoteDomain> results = new ArrayList<NoteDomain>();
		
		try {
			results = noteService.getByMarker(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
//	@POST
//	@ApiOperation(value = "Process")
//	@Path("/process")
//	public Boolean process(String parentKey, NoteDomain noteDomain, String mgiTypeKey, User user) {
//		return noteService.process(parentKey, noteDomain, mgiTypeKey, user);
//	}
//
//	@POST
//	@ApiOperation(value = "Process Allele Combinations by Genotype key")
//	@Path("/processAlleleCombinations")
//	public Boolean processAlleleCombinations(Integer genotypeKey) {
//		
//		try {
//			return noteService.processAlleleCombinations(genotypeKey);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return false;
//		}	
//	}
		
}
