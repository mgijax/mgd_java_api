package org.jax.mgi.mgd.api.model.voc.domain;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Schema
public class SlimVocabularyTermDomain extends BaseDomain {
	// this class used for lookups
	private String vocabKey;
	private String name;
	private List<SlimTermDomain> terms;

}