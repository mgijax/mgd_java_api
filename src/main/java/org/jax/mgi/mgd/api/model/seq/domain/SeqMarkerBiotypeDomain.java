package org.jax.mgi.mgd.api.model.seq.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SeqMarkerBiotypeDomain extends BaseDomain {
	
	private String cacheKey;
    //private String markerKey;
    //private String sequenceKey;
    //private String organismKey;
    //private String refsKey; 
    //private String qualifierKkey;
    private String sequenceProviderKey;
    private String sequenceProvider;
    //private String sequenceTypeKey;
    //private String logicalDBKey;
    //private String markerTypeKey;
    private String biotypeConflictKey;
    private Boolean isBiotypeConflict;
    private String accID;
    private String rawbiotype;   
	//private String createdByKey;
	//private String createdBy;
	//private String modifiedByKey;
	//private String modifiedBy;
	//private String creation_date;
	//private String modification_date;

}
