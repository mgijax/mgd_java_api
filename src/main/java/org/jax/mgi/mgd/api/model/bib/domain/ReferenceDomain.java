package org.jax.mgi.mgd.api.model.bib.domain;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.acc.domain.AccessionDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.MGIReferenceAssocDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ReferenceDomain extends BaseDomain {
	
	private String refsKey;
	private String primaryAuthor;
	private String authors;
	private String title;
	private String journal;
	private String vol;
	private String issue;
	private String date;
	private String year;
	private String pgs;
	private String referenceAbstract;
	private String isReviewArticle;
	private String isDiscard;
	private String referenceTypeKey;
	private String referenceType;
	private String jnumid;
	private String jnum;
	private String short_citation;
	private String createdByKey;
	private String createdBy;
	private String modifiedByKey;
	private String modifiedBy;
	private String creation_date;
	private String modification_date;

	private ReferenceBookDomain referenceBook;
	private ReferenceNoteDomain referenceNote;
	private List<AccessionDomain> mgiAccessionIds;
	private List<AccessionDomain> editAccessionIds;
	private List<MGIReferenceAssocDomain> refAssocs;

	// ReferenceCitationCache not included in domain at this time 
}
