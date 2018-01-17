package org.jax.mgi.mgd.api.rest.interfaces.test;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.rest.interfaces.RESTInterface;	
import org.jax.mgi.mgd.api.model.voc.entities.Evidence;

import io.swagger.annotations.Api;

@Path("/voctext")
@Api(value = "Vocabulary Text Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface VOCEvidenceRESTInterface extends RESTInterface<Evidence> {


}
