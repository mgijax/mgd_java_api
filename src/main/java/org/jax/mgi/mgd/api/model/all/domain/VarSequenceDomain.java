package org.jax.mgi.mgd.api.model.all.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class VarSequenceDomain extends BaseDomain {
	
	private String.allSequenceKey;
	private String.alliantKey;
	private String sequenceTypeKey;
	private String sequenceTypeTerm;
	private String referenceSequence;
	private String.alliantSequence;
	private String createdByKey;
	private String createdBy;
	private String modifiedByKey;
	private String modifiedBy;
	private String creation_date;
	private String modification_date; 
		
}
