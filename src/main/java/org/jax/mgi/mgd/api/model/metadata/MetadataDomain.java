package org.jax.mgi.mgd.api.model.metadata;

import org.jax.mgi.mgd.api.model.BaseDomain;

/* Is: a domain object that represents various pieces of metadata for the API
 */
public class MetadataDomain extends BaseDomain {
	public String database_server;
	public String database_name;
	public String database_dump_date;
	public String logging_level;
	public String api_port;
}
