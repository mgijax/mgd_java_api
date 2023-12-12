package org.jax.mgi.mgd.api.model.img.controller;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.img.domain.GXDImagePaneDomain;
import org.jax.mgi.mgd.api.model.img.domain.ImagePaneDomain;
import org.jax.mgi.mgd.api.model.img.domain.SlimImagePaneDomain;
import org.jax.mgi.mgd.api.model.img.domain.SummaryImagePaneDomain;
import org.jax.mgi.mgd.api.model.img.service.ImagePaneService;
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

@Path("/imagepane")
@Tag(name = "ImagePane Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ImagePaneController extends BaseController<ImagePaneDomain> {

	protected Logger log = Logger.getLogger(getClass());
	ObjectMapper mapper = new ObjectMapper();
	
	@Inject
	private ImagePaneService imagePaneService;

	// refresh/resync the results due to database triggers
	// for example, the mgi accession id is created by a database trigger
	
	@Override
	public SearchResults<ImagePaneDomain> create(ImagePaneDomain domain, User user) {	
		return imagePaneService.create(domain, user);
	}

	@Override
	public SearchResults<ImagePaneDomain> update(ImagePaneDomain domain, User user) {
		return imagePaneService.update(domain, user);
	}

	@Override
	public ImagePaneDomain get(Integer key) {
		return imagePaneService.get(key);
	}

	@Override
	public SearchResults<ImagePaneDomain> delete(Integer key, User user) {
		return imagePaneService.delete(key, user);
	}
	
	@POST
	@Operation(description = "Search")
	@Path("/search")
	public List<ImagePaneDomain> search(Integer key) {
		return imagePaneService.search(key);
	}

	@POST
	@Operation(description = "Get GXD Image Panes by Ref key")
	@Path("/getGXDByReference")
	public List<GXDImagePaneDomain> getGXDByReference(Integer key) {
	
		List<GXDImagePaneDomain> results = new ArrayList<GXDImagePaneDomain>();

		try {
			results = imagePaneService.getGXDByReference(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
	@POST
	@Operation(description = "Get Summary Image Panes by Ref key")
	@Path("/getSummaryByReference")
	public List<SummaryImagePaneDomain> getSummaryByReference(Integer key) {
	
		List<SummaryImagePaneDomain> results = new ArrayList<SummaryImagePaneDomain>();

		try {
			results = imagePaneService.getSummaryByReference(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
	@POST
	@Operation(description = "Process")
	@Path("/process")
	public Boolean process(String parentKey, List<ImagePaneDomain> domain, User user) {
		return imagePaneService.process(parentKey, domain, user);
	}

	@POST
	@Operation(description = "Validate image pane by mgi id/pix id")
	@Path("/validateImagePane")
	public List<SlimImagePaneDomain> validateImagePane(SlimImagePaneDomain searchDomain) {
	
		List<SlimImagePaneDomain> results = new ArrayList<SlimImagePaneDomain>();

		try {
			results = imagePaneService.validateImagePane(searchDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
}
