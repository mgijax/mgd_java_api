package org.jax.mgi.mgd.api.rest.interfaces.test;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.model.mld.entities.MC2point;
import org.jax.mgi.mgd.api.rest.interfaces.RESTInterface;

import io.swagger.annotations.Api;

@Path("/mc2point")
@Api(value = "MC2point Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface MC2pointRESTInterface extends RESTInterface<MC2point> {

}
