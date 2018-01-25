package org.jax.mgi.mgd.api.rest.interfaces.test;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.rest.interfaces.RESTInterface;	
import org.jax.mgi.mgd.api.model.voc.entities.TermEMAPA;

import io.swagger.annotations.Api;

@Path("/term_emapa")
@Api(value = "Vocabulary Term EMAPA Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface TermEMAPARESTInterface extends RESTInterface<TermEMAPA> {


}
