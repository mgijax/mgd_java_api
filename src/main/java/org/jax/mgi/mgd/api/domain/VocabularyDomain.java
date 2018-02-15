package org.jax.mgi.mgd.api.domain;

import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@ApiModel
public class VocabularyDomain extends DomainBase {

	private Integer _vocab_key;
	private Integer isSimple;
	private Integer isPrivate;
	private String name;
	private String note;
	private Date creation_date;
	private Date modification_date;
	private List<TermDomain> terms;

}