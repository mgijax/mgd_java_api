package org.jax.mgi.mgd.api.model.prb.domain;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.acc.domain.AccessionDomain;
import org.jax.mgi.mgd.api.model.acc.domain.SlimAccessionDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.NoteDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProbeDomain extends BaseDomain {

	private String probeKey;
	private String name;
	private String segmentTypeKey;
	private String segmentType;
	private String derivedFromKey;
	private String derivedFromName;
	private String derivedFromAccID;
	private String vectorTypeKey;
	private String vectorType;
	private String primer1sequence;
	private String primer2sequence;
	private String productSize;
	private String regionCovered;
	private String insertSite;
	private String insertSize;
	private String createdByKey;
	private String createdBy;
	private String modifiedByKey;
	private String modifiedBy;
	private String creation_date;
	private String modification_date;
	private String accID;

	private List<SlimAccessionDomain> mgiAccessionIds;
//	private List<AccessionDomain> otherAccessionIds;
	private ProbeSourceDomain probeSource;
	private List<ProbeMarkerDomain> markers;
	private List<ProbeReferenceDomain> references;
	private ProbeNoteDomain generalNote;
	private NoteDomain rawsequenceNote;

}
