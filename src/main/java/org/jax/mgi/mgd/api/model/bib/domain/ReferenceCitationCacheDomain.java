package org.jax.mgi.mgd.api.model.bib.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ReferenceCitationCacheDomain extends BaseDomain {

	private int _refs_key;
	private Integer numericPart; // must be Integer or LT will fail
	private String jnumid;
	private String jnum;
	private String mgiid;
	private String pubmedid;
	private String doiid;
	private String journal;
	private String citation;
	private String short_citation;
	private String year;
	private String referenceType;
	private String relevanceTermKey;
	private String relevanceTerm;
	private int isReviewArticle;
	private String isReviewArticleString;

	// used by validateJnumImage
	private String copyright;
	private Boolean needsDXDOIid = false;
	private Boolean isCreativeCommons = false;
}
