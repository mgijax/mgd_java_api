package org.jax.mgi.mgd.api.rest.interfaces.test;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.model.gxd.entities.GelUnits;
import org.jax.mgi.mgd.api.rest.interfaces.RESTInterface;

import io.swagger.annotations.Api;

@Path("/gelunits")
@Api(value = "GelUnits Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface GelUnitsRESTInterface extends RESTInterface<GelUnits> {

}
