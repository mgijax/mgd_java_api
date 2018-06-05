package org.jax.mgi.mgd.api.model.prb.domain;

import java.util.Date;

import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.bib.entities.Reference;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeMarkerKey;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProbeMarkerDomain extends BaseDomain {

	private ProbeMarkerKey key;
	private String relationship;
	private String createdBy;
	private String modifiedBy;
	private Date creation_date;
	private Date modification_date;
}
