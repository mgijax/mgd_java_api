package org.jax.mgi.mgd.api.model.voc.domain;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Schema(description = "Term DAG Parent Domain")
public class TermDagParentDomain extends BaseDomain {
	// see TermService/getDagParents
	// represents the parent terms of a given child

	private String parentKey;
	private String parentTerm;
	
}

