package org.jax.mgi.mgd.api.model.img.controller;

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
import org.jax.mgi.mgd.api.model.img.domain.ImageDomain;
import org.jax.mgi.mgd.api.model.img.domain.ImageSubmissionDomain;
import org.jax.mgi.mgd.api.model.img.domain.SlimImageDomain;
import org.jax.mgi.mgd.api.model.img.service.ImageService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/image")
@Api(value = "Image Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ImageController extends BaseController<ImageDomain> {

	protected Logger log = Logger.getLogger(getClass());
	ObjectMapper mapper = new ObjectMapper();
	
	@Inject
	private ImageService imageService;

	// refresh/resync the results due to database triggers
	// for example, the mgi accession id is created by a database trigger
	
	@Override
	public SearchResults<ImageDomain> create(ImageDomain domain, User user) {
		SearchResults<ImageDomain> results = new SearchResults<ImageDomain>();
		results = imageService.create(domain, user);
		results = imageService.getResults(Integer.valueOf(results.items.get(0).getImageKey()));
		return results;
	}

	@Override
	public SearchResults<ImageDomain> update(ImageDomain domain, User user) {
		SearchResults<ImageDomain> results = new SearchResults<ImageDomain>();
		results = imageService.update(domain, user);
		results = imageService.getResults(Integer.valueOf(results.items.get(0).getImageKey()));
		return results;	
	}

	@Override
	public ImageDomain get(Integer imageKey) {
		return imageService.get(imageKey);
	}

	@Override
	public SearchResults<ImageDomain> delete(Integer key, User user) {
		return imageService.delete(key, user);
	}

	@GET
	@ApiOperation(value = "Get the object count from img_image table")
	@Path("/getObjectCount")
	public String getObjectCount() {
		return imageService.getObjectCount();
	}
		
	@POST
	@ApiOperation(value = "Search/returns slim image domain")
	@Path("/search")
	public List<SlimImageDomain> search(ImageDomain searchDomain) {
	
		List<SlimImageDomain> results = new ArrayList<SlimImageDomain>();

		log.info("searchDomain/controller: " + searchDomain.getCaptionNote().getNoteChunk());
		try {
			results = imageService.search(searchDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}

	@POST
	@ApiOperation(value = "Search by Jnum/returns image domain with image panes")
	@Path("/searchImagePaneByJnum")
	public List<ImageSubmissionDomain> searchImagePaneByJnum(ImageSubmissionDomain searchDomain) {
	
		List<ImageSubmissionDomain> results = new ArrayList<ImageSubmissionDomain>();

		try {
			results = imageService.searchImagePaneByJnum(searchDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}

}
