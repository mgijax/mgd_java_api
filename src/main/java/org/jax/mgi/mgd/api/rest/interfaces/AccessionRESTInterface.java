package org.jax.mgi.mgd.api.rest.interfaces;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.model.acc.entities.Accession;

import io.swagger.annotations.Api;

@Path("/accession")
@Api(value = "Accession Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface AccessionRESTInterface extends RESTInterface<Accession> {


}
