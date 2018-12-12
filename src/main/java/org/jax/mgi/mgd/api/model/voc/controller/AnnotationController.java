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
import org.jax.mgi.mgd.api.model.voc.domain.AnnotationDomain;
import org.jax.mgi.mgd.api.model.voc.service.AnnotationService;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/vocannotation")
@Api(value = "Voc Annotation Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AnnotationController extends BaseController<AnnotationDomain> {

	@Inject
	private AnnotationService annotationService;

	@Override
	public SearchResults<AnnotationDomain> create(AnnotationDomain object, User user) {
		return annotationService.create(object, user);
	}

	@Override
	public SearchResults<AnnotationDomain> update(AnnotationDomain object, User user) {
		return annotationService.update(object, user);
	}

	@Override
	public AnnotationDomain get(Integer key) {
		return annotationService.get(key);
	}

	@Override
	public SearchResults<AnnotationDomain> delete(Integer key, User user) {
		return annotationService.delete(key, user);
	}

	@POST
	@ApiOperation(value = "Marker/Feature Types")
	@Path("/markerFeatureTypes")
	public List<AnnotationDomain> markerMCV(Integer key) {
			
		List<AnnotationDomain> results = new ArrayList<AnnotationDomain>();
		
		try {
			results = annotationService.markerFeatureTypes(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}

}
