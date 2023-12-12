package org.jax.mgi.mgd.api.model.voc.domain;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Schema(description = "TermFamilyView Domain")
public class TermFamilyViewDomain extends BaseDomain {

	private String termKey;
	private String searchid;
	private String accid;
	private String term;
	private String vocabKey;
	private String abbreviation;
	private String note;
	private String sequenceNum;
	private String isObsolete;
	private String createdByKey;
	private String modifiedByKey;
	private String creation_date;
	private String modification_date;
	
}

