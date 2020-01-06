package org.jax.mgi.mgd.api.model.bib.domain;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;
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

	// has to match the LTReferenceDomain
	public String mgiid;	
	public String doiid;
	public String pubmedid;
	public String gorefid;
	
	public String book_author;
	public String book_title;
	public String place;
	public String publisher;
	public String series_ed;
	public String referenceNote;

	// workflow status
	public String ap_status;
	public String go_status;
	public String gxd_status;
	public String qtl_status;
	public String tumor_status;
	
	//private ReferenceBookDomain referenceBook;
	//private ReferenceNoteDomain referenceNote;
	//private List<AccessionDomain> mgiAccessionIds;
	//private List<AccessionDomain> editAccessionIds;
	
	// these lists are loaded by the pwi at runtime
	private List<MGIReferenceAssocDomain> alleleAssocs;
	private List<MGIReferenceAssocDomain> markerAssocs;
	private List<MGIReferenceAssocDomain> strainAssocs;

	// ReferenceCitationCache not included in domain at this time 
}
