package org.jax.mgi.mgd.api.model.mgi.domain;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.all.domain.SlimAlleleRefAssocDomain;
import org.jax.mgi.mgd.api.model.mrk.domain.SlimMarkerDomain;
import org.jax.mgi.mgd.api.model.prb.domain.SlimProbeStrainDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MGIReferenceAssocDomain extends BaseDomain {

	private String processStatus;
	private String assocKey;
	private String objectKey;
	private String mgiTypeKey;
	private String refAssocTypeKey;
	private String refAssocType;
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
	
	//private List<SlimAlleleRefAssocDomain> alleles;
	//private List<SlimMarkerDomain> markers;
	//private List<SlimProbeStrainDomain> strains;

}   	