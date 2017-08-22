package org.jax.mgi.mgd.api.rest.interfaces;


import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.domain.MetadataDomain;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/metadata")
@Api(value = "Metadata Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface MetadataRESTInterface {

	@GET
	@ApiOperation(value = "Value: Retrieves various metadata", notes="Notes: Retrieves various metadata")
	public MetadataDomain get();

}
