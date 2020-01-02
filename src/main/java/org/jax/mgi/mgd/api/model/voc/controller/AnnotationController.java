package org.jax.mgi.mgd.api.model.voc.controller;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.domain.AnnotationDomain;
import org.jax.mgi.mgd.api.model.voc.service.AnnotationService;
import org.jax.mgi.mgd.api.util.SearchResults;

import io.swagger.annotations.Api;

@Path("/vocannotation")
@Api(value = "Voc Annotation Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AnnotationController extends BaseController<AnnotationDomain> {

	@Inject
	private AnnotationService annotationService;

	@Override
	public SearchResults<AnnotationDomain> create(AnnotationDomain domain, User user) {
		return annotationService.create(domain, user);
	}

	@Override
	public SearchResults<AnnotationDomain> update(AnnotationDomain domain, User user) {
		return annotationService.update(domain, user);
	}

	@Override
	public AnnotationDomain get(Integer key) {
		return annotationService.get(key);
	}

	@Override
	public SearchResults<AnnotationDomain> delete(Integer key, User user) {
		return annotationService.delete(key, user);
	}
		
}
