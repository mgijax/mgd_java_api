package org.jax.mgi.mgd.api.model.voc.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@ApiModel(value = "Slim Term Domain")
public class SlimTermDomain extends BaseDomain {

	private String vocabKey;
	private String termKey;
	private Integer _term_key;
	private String term;
	private String abbreviation;
	
}
