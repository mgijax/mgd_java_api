package org.jax.mgi.mgd.api.model.bib.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ReferenceDomain extends BaseDomain {
	
	private String refsKey;
	private String primaryAuthor;
	private String authors;
	private String title;
	private String journal;
	private String volume;
	private String issue;
	private String date;
	private String year;
	private String pages;
	private String isReviewArticle;
	private String isDiscard;
	private String referenceTypeKey;
	private String referenceType;
	private String jnumID;
	private String jnum;
	private String short_citation;
	private String createdByKey;
	private String createdBy;
	private String modifiedByKey;
	private String modifiedBy;
	private String creation_date;
	private String modification_date;
	
}
