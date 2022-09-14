package org.jax.mgi.mgd.api.model.voc.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@ApiModel(value = "TermFamilyView Domain")
public class TermFamilyViewDomain extends BaseDomain {

	private String termKey;
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

