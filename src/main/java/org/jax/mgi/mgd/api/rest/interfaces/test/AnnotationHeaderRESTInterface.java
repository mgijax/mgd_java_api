package org.jax.mgi.mgd.api.rest.interfaces.test;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.rest.interfaces.RESTInterface;	
import org.jax.mgi.mgd.api.model.voc.entities.AnnotationHeader;

import io.swagger.annotations.Api;

@Path("/annotheader")
@Api(value = "Annotation Header Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface AnnotationHeaderRESTInterface extends RESTInterface<AnnotationHeader> {


}
