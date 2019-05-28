package org.jax.mgi.mgd.api.model.img.controller;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.img.domain.ImagePaneAssocDomain;
import org.jax.mgi.mgd.api.model.img.service.ImagePaneAssocService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/imagepaneassoc")
@Api(value = "ImagePaneAssoc Endpoints")
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
	@ApiOperation(value = "Search")
	@Path("/search")
	public List<ImagePaneAssocDomain> search(Integer key) {
		return imagePaneAssocService.search(key);
	}
	
	@POST
	@ApiOperation(value = "Process")
	@Path("/process")
	public Boolean process(String parentKey, List<ImagePaneAssocDomain> domain, User user) {
		return imagePaneAssocService.process(parentKey, domain, user);
	}

}
