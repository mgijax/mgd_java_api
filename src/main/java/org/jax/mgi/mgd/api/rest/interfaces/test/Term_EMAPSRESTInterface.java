package org.jax.mgi.mgd.api.rest.interfaces.test;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.rest.interfaces.RESTInterface;	
import org.jax.mgi.mgd.api.model.voc.entities.Term_EMAPS;

import io.swagger.annotations.Api;

@Path("/term_emaps")
@Api(value = "Vocabulary Term EMAPS Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface Term_EMAPSRESTInterface extends RESTInterface<Term_EMAPS> {


}
