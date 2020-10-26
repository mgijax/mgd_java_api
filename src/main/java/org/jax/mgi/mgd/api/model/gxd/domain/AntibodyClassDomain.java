package org.jax.mgi.mgd.api.model.gxd.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AntibodyClassDomain extends BaseDomain {

	private String processStatus;
	private String termKey;
	private String term;
	private String vocabKey;
	private String abbreviation;
	private String note;
	private String sequenceNum;
	private Boolean includeObsolete = Boolean.FALSE;
	private String creation_date;
	private String modification_date;
}
