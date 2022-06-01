package org.jax.mgi.mgd.api.model.bib.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ReferenceCitationCacheDomain extends BaseDomain {
	
	private String refsKey;
	private String numericPart;
	private String jnumid;
	private String mgiid;
	private String pubmedid;
	private String doiid;
	private String journal;
	private String citation;
	private String short_citation;
	private String referenceType;
	private String referenceTypeKey;
	private String relevanceTerm;
	private String isReviewArticle;
	private String isReviewArticleString;

}
