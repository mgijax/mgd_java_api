package org.jax.mgi.mgd.api.rest.interfaces;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.domain.MGITypeDomain;

import io.swagger.annotations.Api;

@Path("/mgitype")
@Api(value = "MGI Type Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface MGITypeRESTInterface extends RESTInterface<MGITypeDomain> {

}
