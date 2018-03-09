package org.jax.mgi.mgd.api.model.voc.domain;

import java.util.Date;

import org.jax.mgi.mgd.api.model.BaseDomain;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@ApiModel(value = "Term Model Object")
public class TermDomain extends BaseDomain {

	private Integer _term_key;
	private String term;
	private String abbreviation;
	private String note;
	private Integer sequenceNum; // This is set automatically via the UI
	private Integer isObsolete = 0; // Don't allow the user through UI to set this
	private Date creation_date;
	private Date modification_date;

	private String createdBy;
	private String modifiedBy;
	private String vocabName;
}
