package org.jax.mgi.mgd.api.model.prb.domain;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.acc.domain.AccessionDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProbeReferenceDomain extends BaseDomain {

	private String processStatus;
	private String referenceKey;
	private String probeKey;
	private String hasRmap;
	private String hasSequence;
	private String refsKey;
	private String jnumid;
	private Integer jnum;
	private String short_citation;
	private String createdByKey;
	private String createdBy;
	private String modifiedByKey;
	private String modifiedBy;
	private String creation_date;
	private String modification_date;
	
	private List<AccessionDomain> accessionIds;
	private List<ProbeAliasDomain> aliases;
	
	// note:  these tables are no longer being edited, 
	// but are included in the domain for display purposes (the probe summary page)
	private ProbeReferenceNoteDomain referenceNote;
	private List<ProbeRFLVDomain> rflvs;
}
