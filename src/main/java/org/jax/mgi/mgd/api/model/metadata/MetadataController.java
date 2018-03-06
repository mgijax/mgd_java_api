package org.jax.mgi.mgd.api.model.metadata;

import javax.inject.Inject;

public class MetadataController implements MetadataRESTInterface {

	@Inject
	private MetadataService metadataService;

	public MetadataDomain get() {
		return metadataService.get();
	}
}
