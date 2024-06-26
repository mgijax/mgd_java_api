package org.jax.mgi.mgd.api.model.img.controller;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.img.domain.ImagePaneAssocDomain;
import org.jax.mgi.mgd.api.model.img.service.ImagePaneAssocService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/imagepaneassoc")
@Tag(name = "ImagePaneAssoc Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ImagePaneAssocController extends BaseController<ImagePaneAssocDomain> {

	protected Logger log = Logger.getLogger(getClass());
	ObjectMapper mapper = new ObjectMapper();
	
	@Inject
	private ImagePaneAssocService imagePaneAssocService;

	// refresh/resync the results due to database triggers
	// for example, the mgi accession id is created by a database trigger
	
	@Override
	public SearchResults<ImagePaneAssocDomain> create(ImagePaneAssocDomain domain, User user) {	
		return imagePaneAssocService.create(domain, user);
	}

	@Override
	public SearchResults<ImagePaneAssocDomain> update(ImagePaneAssocDomain domain, User user) {
		return imagePaneAssocService.update(domain, user);
	}

	@Override
	public ImagePaneAssocDomain get(Integer imageKey) {
		return imagePaneAssocService.get(imageKey);
	}

	@Override
	public SearchResults<ImagePaneAssocDomain> delete(Integer key, User user) {
		// this table contains a compound primary key
		// deletes to this table are implemented in the parent's "update" method
		return null;
	}
	
	@POST
	@Operation(description = "Search")
	@Path("/search")
	public List<ImagePaneAssocDomain> search(Integer key) {
		return imagePaneAssocService.search(key);
	}
	
	@POST
	@Operation(description = "Process")
	@Path("/process")
	public Boolean process(String parentKey, List<ImagePaneAssocDomain> domain, User user) {
		return imagePaneAssocService.process(parentKey, domain, user);
	}
	
}
