package org.jax.mgi.mgd.api.domain;

import java.util.Date;

import org.jax.mgi.mgd.api.model.mgi.entities.User;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@ApiModel
public class TermDomain extends DomainBase {

	private Integer _term_key;
	private String term;
	private String abbreviation;
	private Integer sequenceNum;
	private Integer isObsolete;
	private Date creation_date;
	private Date modification_date;

	private String createdBy;
	private String modifiedBy;
	private String vocabName;
}
