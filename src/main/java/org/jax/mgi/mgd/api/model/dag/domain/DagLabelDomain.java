package org.jax.mgi.mgd.api.model.dag.domain;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Schema(description = "DagLabel Domain")
public class DagLabelDomain extends BaseDomain {

	private String labelKey;
	private String label;
	private String creation_date;
	private String modification_date;
}
