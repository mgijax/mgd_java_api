package org.jax.mgi.mgd.api.model.voc.domain;

import java.util.Date;

import org.jax.mgi.mgd.api.model.DomainBase;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@ApiModel(value = "Term Model Object")
public class TermDomain extends DomainBase {

	private Integer _term_key;
	private String term;
	private String abbreviation;
	private String note;
	private Integer sequenceNum;
	private Integer isObsolete = 0;
	private Date creation_date;
	private Date modification_date;

	private String createdBy;
	private String modifiedBy;
	private String vocabName;
}
