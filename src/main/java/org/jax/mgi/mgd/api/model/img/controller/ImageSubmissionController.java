package org.jax.mgi.mgd.api.model.img.controller;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.model.BaseController;
import org.jax.mgi.mgd.api.model.img.domain.ImageSubmissionDomain;
import org.jax.mgi.mgd.api.model.img.service.ImageSubmissionService;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.SearchResults;
import org.jboss.logging.Logger;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/imageSubmission")
@Api(value = "Image Submission Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ImageSubmissionController extends BaseController<ImageSubmissionDomain> {

	protected Logger log = Logger.getLogger(getClass());
	ObjectMapper mapper = new ObjectMapper();
	
	@Inject
	private ImageSubmissionService imageSubmissionService;

	// refresh/resync the results due to database triggers
	// for example, the mgi accession id is created by a database trigger
	
	@Override
	public SearchResults<ImageSubmissionDomain> create(ImageSubmissionDomain domain, User user) {
		SearchResults<ImageSubmissionDomain> results = new SearchResults<ImageSubmissionDomain>();
		results = imageSubmissionService.create(domain, user);
		results = imageSubmissionService.getResults(Integer.valueOf(results.items.get(0).getImageKey()));
		return results;
	}

	@Override
	public SearchResults<ImageSubmissionDomain> update(ImageSubmissionDomain domain, User user) {
		SearchResults<ImageSubmissionDomain> results = new SearchResults<ImageSubmissionDomain>();
		results = imageSubmissionService.update(domain, user);
		results = imageSubmissionService.getResults(Integer.valueOf(results.items.get(0).getImageKey()));
		return results;	
	}

	@Override
	public ImageSubmissionDomain get(Integer imageKey) {
		return imageSubmissionService.get(imageKey);
	}

	@Override
	public SearchResults<ImageSubmissionDomain> delete(Integer key, User user) {
		return imageSubmissionService.delete(key, user);
	}

	@POST
	@ApiOperation(value = "Search/returns image submission domain")
	@Path("/search")
	public List<ImageSubmissionDomain> search(ImageSubmissionDomain searchDomain) {
	
		List<ImageSubmissionDomain> results = new ArrayList<ImageSubmissionDomain>();

		try {
			results = imageSubmissionService.search(searchDomain);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}

	@POST
	@Path("/submit")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public SearchResults<ImageSubmissionDomain> submit(			
			@HeaderParam(value="api_access_token") String api_access_token,
			@HeaderParam(value="username") String username,
			MultipartFormDataInput input) {
	
		SearchResults<ImageSubmissionDomain> results = new SearchResults<ImageSubmissionDomain>();		
		
		try {
			Boolean userToken = authenticateToken(api_access_token);
			User user = authenticateUser(username);
			
			if (userToken && user != null) {		
				results = imageSubmissionService.submit(input);
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
