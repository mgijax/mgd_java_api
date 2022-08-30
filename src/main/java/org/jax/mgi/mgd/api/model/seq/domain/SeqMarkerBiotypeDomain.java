package org.jax.mgi.mgd.api.model.seq.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SeqMarkerBiotypeDomain extends BaseDomain {
		
	private String cacheKey;
    private String sequenceProviderKey;
    private String sequenceProvider;
    private String biotypeConflictKey;
    private Boolean isBiotypeConflict;
    private String accID;
    private String rawbiotype;   

}
