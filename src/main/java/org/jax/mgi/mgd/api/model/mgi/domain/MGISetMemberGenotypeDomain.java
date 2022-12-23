package org.jax.mgi.mgd.api.model.mgi.domain;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.SlimAllelePairDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MGISetMemberGenotypeDomain extends BaseDomain {

	private String setKey;
	private String setMemberKey;
	private String objectKey;
	private String label;
	private String displayIt;
	private String createdByKey;
	private String createdBy;	
	List<SlimAllelePairDomain> allelePairs;
	
}   	