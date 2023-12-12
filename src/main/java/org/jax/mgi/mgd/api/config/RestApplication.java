package org.jax.mgi.mgd.api.config;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("/api")
@ApplicationScoped
@OpenAPIDefinition(
    info = @Info(
          description = "This is the MGI Informatics MGD Java API for access to the Database",
          title = "MGI MGD API",
          version = "1.0")
)
public class RestApplication extends Application {

}
