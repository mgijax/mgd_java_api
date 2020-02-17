package org.jax.mgi.mgd.api.model.gxd.domain;

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
	private String createdByKey;
	private String createdBy;
	private String modifiedByKey;
	private String modifiedBy;
	private String creation_date;
	private String modification_date;
	
	// may be null
	private String antibodyPrepKey;
	private String antibodyPrepName;
	private String antibodyPrepSecondaryKey;
	private String antibodyPrepSecondary;	
	private String antibodyPrepLabelKey;
	private String antibodyPrepLabel;
	private String antibodyAccID;

	// may be null
	private String probePrepKey;
	private String probePrepType;
	private String probePrepSenseKey;
	private String probePrepSense;
	private String probePrepLabelKey;
	private String probePrepLabel;
	private String probePrepVisualizationKey;
	private String probePrepVisualiation;
	private String probeAccID;

	// may be null
	private String imagePaneKey;
	private String imagePaneLabel;
	
	// may be null
	private String reporterGeneKey;
	private String reporterGene;
	
}
