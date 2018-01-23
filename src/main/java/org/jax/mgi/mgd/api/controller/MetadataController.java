package org.jax.mgi.mgd.api.controller;

import javax.inject.Inject;

import org.jax.mgi.mgd.api.domain.MetadataDomain;
import org.jax.mgi.mgd.api.rest.interfaces.MetadataRESTInterface;
import org.jax.mgi.mgd.api.service.MetadataService;
import org.jboss.logging.Logger;

public class MetadataController extends BaseController implements MetadataRESTInterface {

	@Inject
	private MetadataService metadataService;

	private Logger log = Logger.getLogger(getClass());

	@Override
	public MetadataDomain get() {
		return metadataService.get();
	}
}
