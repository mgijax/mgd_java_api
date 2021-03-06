package org.jax.mgi.mgd.api.model.all.domain;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.acc.domain.AccessionDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class VariantSequenceDomain extends BaseDomain {

	private String processStatus;		
	private String variantSequenceKey;
	private String variantKey;
	private String sequenceTypeKey;
	private String sequenceTypeTerm;
    private String startCoordinate;
    private String endCoordinate;
	private String referenceSequence;
	private String variantSequence;
	private String version;
	private String createdByKey;
	private String createdBy;
	private String modifiedByKey;
	private String modifiedBy;
	private String creation_date;
	private String modification_date; 
	
	private List<AccessionDomain> accessionIds;
	
}
