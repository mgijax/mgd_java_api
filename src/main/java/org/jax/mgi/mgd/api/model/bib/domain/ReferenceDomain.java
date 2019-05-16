package org.jax.mgi.mgd.api.model.bib.domain;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.acc.domain.AccessionDomain;

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
	private String referenceAbstract;
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

	private List<ReferenceBookDomain> referenceBook;
	private List<AccessionDomain> mgiAccessionIds;
	private List<AccessionDomain> editAccessionIds;
	
	// add Notes
	// ReferenceCitationCache not included in domain at this time 
}
