package org.jax.mgi.mgd.api.model.prb.domain;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProbeDomain extends BaseDomain {

	private String processStatus;
	private String probeKey;
	private String name;
	private String derivedFrom;
	private String primer1sequence;
	private String primer2sequence;
	private String regionCovered;
	private String insertSite;
	private String insertSize;
	private String productSize;
	private String createdByKey;
	private String createdBy;
	private String modifiedByKey;
	private String modifiedBy;
	private String creation_date;
	private String modification_date;
	private String accID;

	private List<ProbeMarkerDomain> markers;
	private ProbeNotesDomain generalNote;
	//private NoteDomain generalNote;
	
}
