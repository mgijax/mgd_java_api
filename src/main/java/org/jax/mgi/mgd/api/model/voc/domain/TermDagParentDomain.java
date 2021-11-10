package org.jax.mgi.mgd.api.model.voc.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@ApiModel(value = "Term DAG Parent Domain")
public class TermDagParentDomain extends BaseDomain {
	// see TermService/getDagParents
	// represents the parent terms of a given child

	private String termKey;
	private String term;
	private String parentKey;
	private String parentTerm;
	
}

