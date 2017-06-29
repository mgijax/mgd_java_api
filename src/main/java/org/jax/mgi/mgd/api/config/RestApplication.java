package org.jax.mgi.mgd.api.config;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/api")
@ApplicationScoped
public class RestApplication extends Application {

}
