package org.jax.mgi.mgd.api.rest.interfaces;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.entities.Vocabulary;

import io.swagger.annotations.Api;

@Path("/vocab")
@Api(value = "Vocab Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface VocabRESTInterface extends RESTInterface<Vocabulary> {


}
