package org.jax.mgi.mgd.api.model.img.controller;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.img.domain.ImageDomain;
import org.jax.mgi.mgd.api.model.img.domain.ImageSubmissionDomain;
import org.jax.mgi.mgd.api.model.img.domain.SlimImageDomain;
import org.jax.mgi.mgd.api.model.img.service.ImagePaneAssocService;
import org.jax.mgi.mgd.api.model.img.service.ImageService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.Constants;
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
	@Inject
	private ImagePaneAssocService imagePaneAssocService;
	
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
	public SearchResults<ImageDomain> getObjectCount() {
		return imageService.getObjectCount();
	}
		
	@POST
	@ApiOperation(value = "Search/returns slim image domain")
	@Path("/search")
	public List<SlimImageDomain> search(ImageDomain searchDomain) {
	
		List<SlimImageDomain> results = new ArrayList<SlimImageDomain>();

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

	@PUT
	@Path("/updateAlleleAssoc")
	public SearchResults<ImageDomain> updateAlleleAssoc(			
			@HeaderParam(value="api_access_token") String api_access_token,
			@HeaderParam(value="username") String username,
			ImageDomain domain) {
	
		SearchResults<ImageDomain> results = new SearchResults<ImageDomain>();		
		
		try {
			Boolean userToken = authenticateToken(api_access_token);
			User user = authenticateUser(username);
			
			if (userToken && user != null) {		
				imagePaneAssocService.deleteAlleleAssoc(domain, user);
				results = imagePaneAssocService.updateAlleleAssoc(domain, user);
				results = imageService.getResults(Integer.valueOf(domain.getImageKey()));		
				log.info(Constants.LOG_OUT_DOMAIN);
			} else {
				results.setError(Constants.LOG_FAIL_USERAUTHENTICATION, api_access_token + "," + username, Constants.HTTP_SERVER_ERROR);
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;		
	}
		
}
