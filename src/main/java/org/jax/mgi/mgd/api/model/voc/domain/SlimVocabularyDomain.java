package org.jax.mgi.mgd.api.model.voc.domain;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@ApiModel
public class SlimVocabularyDomain extends BaseDomain {

	private String vocabKey;
	private String name;
	// for backward compatibility with gxd/littriage 	
	private String vocabName;
	private List<SlimTermDomain> terms;

}