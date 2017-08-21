package org.jax.mgi.mgd.api.service;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.jax.mgi.mgd.api.dao.MarkerDAO;
import org.jax.mgi.mgd.api.dao.PostgresSQLDAO;
import org.jax.mgi.mgd.api.domain.MetadataDomain;
import org.jax.mgi.mgd.api.entities.Marker;

@RequestScoped
public class MetadataService {

	@Inject
	private PostgresSQLDAO postgresDAO;
	
	public MetadataDomain get() {
		MetadataDomain md = new MetadataDomain();
		md.logging_level = System.getProperty("swarm.logging");
		md.api_port = System.getProperty("swarm.http.port");
		md.database_dump_date = postgresDAO.getDumpDate();
		
		if (md.api_port == null) {
			md.api_port = "8080";		// JBoss default
		}
		
		String url = System.getProperty("swarm.ds.connection.url");
		if (url != null) {
			Pattern pattern = Pattern.compile(".*\\/\\/([^:]+):([^\\/]+)\\/(.+)");
			Matcher matcher = pattern.matcher(url);
			if (matcher.find()) {
				md.database_server = matcher.group(1);
				md.database_name = matcher.group(3);
			}
		}
		return md;
	}
}
