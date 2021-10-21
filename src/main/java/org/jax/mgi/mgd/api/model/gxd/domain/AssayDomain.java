package org.jax.mgi.mgd.api.model.gxd.domain;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AssayDomain extends BaseDomain {

	private String assayKey;
	private String assayDisplay;		
	private String assayTypeKey;
	private String assayType;
	private String assayTypeAbbrev;
	private Boolean isInSitu = false;
	private Boolean isReporter = false;
	private Boolean isGel = false;
	private Boolean isAntibodyPrep = false;
	private Boolean isProbePrep = false;
	private String markerKey;
	private String markerSymbol;
	private String markerName;
	private String markerAccID;
	private String refsKey;
	private String jnumid;
	private String jnum;
	private String short_citation;
	private String accID;	
	private String imagePaneKey;
	private String reporterGeneKey;
	private String reporterGeneTerm;
	private String createdByKey;
	private String createdBy;
	private String modifiedByKey;
	private String modifiedBy;
	private String creation_date;
	private String modification_date;
	
	/* 1 = nucleotide, 2 = antibody, 3 = direct detection */
	private String detectionKey;
	private String detectionMethod;
	private AntibodyPrepDomain antibodyPrep;
	private ProbePrepDomain probePrep;

	private AssayNoteDomain assayNote;	
	private List<AssayImageViewDomain> imagePanes;
	private List<SpecimenDomain> specimens;
	private List<GelLaneDomain> gelLanes;
	private List<GelRowDomain> gelRows;
	
}
