package org.jax.mgi.mgd.api.rest.interfaces.test;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.model.mgi.entities.SynonymType;
import org.jax.mgi.mgd.api.rest.interfaces.RESTInterface;

import io.swagger.annotations.Api;

@Path("/synonymtype")
@Api(value = "SynonymType Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface SynonymTypeRESTInterface extends RESTInterface<SynonymType> {


}
