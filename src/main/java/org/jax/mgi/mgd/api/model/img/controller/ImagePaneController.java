package org.jax.mgi.mgd.api.model.img.controller;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/imagepane")
@Api(value = "ImagePane Endpoints")
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
	@ApiOperation(value = "Search")
	@Path("/search")
	public List<ImagePaneDomain> search(Integer key) {
		return imagePaneService.search(key);
	}

	@POST
	@ApiOperation(value = "Get GXD Image Panes by Ref key")
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
	
	@GET
	@ApiOperation(value = "Get list of image pane domains by reference jnum id")
	@Path("/getImagePaneByRef")
	public SearchResults<SummaryImagePaneDomain> getImagePaneByRef(
		@QueryParam("accid") String accid,
		@QueryParam("offset") int offset,
		@QueryParam("limit") int limit
		) {

		SearchResults<SummaryImagePaneDomain> results = new SearchResults<SummaryImagePaneDomain>();

		try {
			results = imagePaneService.getImagePaneByRef(accid, offset, limit);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
	@GET
	@ApiOperation(value = "Download TSV file.")
	@Path("/downloadImagePaneByRef")
	@Produces(MediaType.TEXT_PLAIN)
	public Response downloadMarkerByRef(@QueryParam("accid") String accid) {
		return imagePaneService.downloadImagePaneByRef(accid);
	}	
	
	@POST
	@ApiOperation(value = "Process")
	@Path("/process")
	public Boolean process(String parentKey, List<ImagePaneDomain> domain, User user) {
		return imagePaneService.process(parentKey, domain, user);
	}

	@POST
	@ApiOperation(value = "Validate image pane by mgi id/pix id")
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
