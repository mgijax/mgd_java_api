package org.jax.mgi.mgd.api.model.voc.controller;

import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.domain.AnnotationDomain;
import org.jax.mgi.mgd.api.model.voc.service.AnnotationService;
import org.jax.mgi.mgd.api.util.SearchResults;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/vocannotation")
@Tag(name = "Voc Annotation Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AnnotationController extends BaseController<AnnotationDomain> {

	@Inject
	private AnnotationService annotationService;

	@Override
	public SearchResults<AnnotationDomain> create(AnnotationDomain domain, User user) {
		SearchResults<AnnotationDomain> results = new SearchResults<AnnotationDomain>();
		results = annotationService.create(domain, user);
		return results;		
	}

	@Override
	public SearchResults<AnnotationDomain> update(AnnotationDomain domain, User user) {
		SearchResults<AnnotationDomain> results = new SearchResults<AnnotationDomain>();
		results = annotationService.update(domain, user);
		return results;
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
