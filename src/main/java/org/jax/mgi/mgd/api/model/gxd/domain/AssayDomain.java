package org.jax.mgi.mgd.api.model.gxd.domain;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AssayDomain extends BaseDomain {

	private String assayKey;
	private String assayTypeKey;
	private String assayType;
	private String markerKey;
	private String markerSymbol;
	private String refsKey;
	private String jnumid;
	private String jnum;
	private String short_citation;
	private String accID;	
	private String imagePaneKey;
	private String imagePaneLabel;
	private String imageFigureLabel;
	private String reporterGeneKey;
	private String reporterGeneTerm;
	private String createdByKey;
	private String createdBy;
	private String modifiedByKey;
	private String modifiedBy;
	private String creation_date;
	private String modification_date;
	
	private AntibodyPrepDomain antibodyPrep;
	private ProbePrepDomain probePrep;
	private AssayNoteDomain assayNote;	
	private List<SpecimenDomain> specimens;
	
}
