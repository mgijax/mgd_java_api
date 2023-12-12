package org.jax.mgi.mgd.api.model.acc.domain;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Schema(description = "AccessionReference Domain")
public class AccessionReferenceDomain extends BaseDomain {

	private String accessionKey;
	private String refsKey;
	private String jnumid;
	private String jnum;
	private String short_citation;	
	private String createdByKey;
	private String createdBy;
	private String modifiedByKey;
	private String modifiedBy;
	private String creation_date;
	private String modification_date;
}
