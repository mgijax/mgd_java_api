package org.jax.mgi.mgd.api.model.voc.domain;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Schema(description = "Slim Term Domain")
public class SlimTermDomain extends BaseDomain {

	private String vocabKey;
	private String termKey;
	private Integer _term_key;
	private String term;
	private String abbreviation;
	private String primaryid;
}
