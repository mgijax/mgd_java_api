package org.jax.mgi.mgd.api.rest.interfaces;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.model.acc.entities.AccessionMax;

import io.swagger.annotations.Api;

@Path("/accessionmax")
@Api(value = "AccessionMax Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface AccessionMaxRESTInterface extends RESTInterface<AccessionMax> {


}
