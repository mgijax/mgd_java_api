package org.jax.mgi.mgd.api.rest.interfaces.test;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jax.mgi.mgd.api.model.gxd.entities.AssayType;
import org.jax.mgi.mgd.api.rest.interfaces.RESTInterface;

import io.swagger.annotations.Api;

<<<<<<< HEAD:src/main/java/org/jax/mgi/mgd/api/rest/interfaces/test/GXDAssayTypeRESTInterface.java
@Path("/gxdassaytype")
@Api(value = "GXD Assay Type Endpoints")
=======
@Path("/mldassaytype")
@Api(value = "MLD Assay Type Endpoints")
>>>>>>> 5a01b6fe8960f87dc732cef23afe3aef712df9bb:src/main/java/org/jax/mgi/mgd/api/rest/interfaces/test/AssayTypeRESTInterface.java
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface GXDAssayTypeRESTInterface extends RESTInterface<AssayType> {

}
