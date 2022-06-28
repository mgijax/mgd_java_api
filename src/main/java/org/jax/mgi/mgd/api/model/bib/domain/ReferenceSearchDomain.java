package org.jax.mgi.mgd.api.model.bib.domain;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.MGIReferenceAlleleAssocDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.MGIReferenceMarkerAssocDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.MGIReferenceStrainAssocDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ReferenceSearchDomain extends BaseDomain {
	
	// used by ReferenceController/ReferenceService/search() method
	// similar to but not exactly the same as ReferenceDomain/get() method
	
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
	private String mgiid;	
	private String doiid;
	private String pubmedid;
	private String gorefid;
		
	private ReferenceBookDomain referenceBook;
	private ReferenceNoteDomain referenceNote;
	private ReferenceWorkflowDataDomain workflowData;
	
	// PWI loads this data via calls to MGIReferenceAssocService/getXXX()
	private List<MGIReferenceAlleleAssocDomain> alleleAssocs;
	private List<MGIReferenceStrainAssocDomain> strainAssocs;
	private List<MGIReferenceMarkerAssocDomain> markerAssocs;
	
	// other searching parameters
	
	private String currentRelevance;
	private String workflow_tag_operator = "AND";
	private String status_operator = "OR";
	private String orderBy = "1";
	private String accids;
	private String supplementalTerm;
	private String supplementalKey;
	
	// status history
	private String sh_status;
	private String sh_group;
	private String sh_username;
	private String sh_date;
	
	// relevance history
	// TBD : rename as "rh" to match "sh" above
	private String relevance;
	private String relevance_date;
	private String relevance_user;
	private String relevance_version;
	private String relevance_confidence;
	
	private String workflow_tag1;
	private String workflow_tag2;
	private String workflow_tag3;
	private String workflow_tag4;
	private String workflow_tag5;
	private Boolean not_workflow_tag1;
	private Boolean not_workflow_tag2;
	private Boolean not_workflow_tag3;
	private Boolean not_workflow_tag4;
	private Boolean not_workflow_tag5;
	
	private Integer status_AP_New;
	private Integer status_AP_Not_Routed;
	private Integer status_AP_Routed;
	private Integer status_AP_Chosen;
	private Integer status_AP_Indexed;
	private Integer status_AP_Full_coded;
	private Integer status_AP_Rejected;
	
	private Integer status_GO_New;
	private Integer status_GO_Not_Routed;
	private Integer status_GO_Routed;
	private Integer status_GO_Chosen;
	private Integer status_GO_Indexed;
	private Integer status_GO_Full_coded;
	private Integer status_GO_Rejected;	
	
	private Integer status_GXD_New;
	private Integer status_GXD_Not_Routed;
	private Integer status_GXD_Routed;
	private Integer status_GXD_Chosen;
	private Integer status_GXD_Indexed;
	private Integer status_GXD_Full_coded;
	private Integer status_GXD_Rejected;	
	
	private Integer status_PRO_New;
	private Integer status_PRO_Not_Routed;
	private Integer status_PRO_Routed;
	private Integer status_PRO_Chosen;
	private Integer status_PRO_Indexed;
	private Integer status_PRO_Full_coded;
	private Integer status_PRO_Rejected;	
	
	private Integer status_QTL_New;
	private Integer status_QTL_Not_Routed;
	private Integer status_QTL_Routed;
	private Integer status_QTL_Chosen;
	private Integer status_QTL_Indexed;
	private Integer status_QTL_Full_coded;
	private Integer status_QTL_Rejected;		
	
	private Integer status_Tumor_New;
	private Integer status_Tumor_Not_Routed;
	private Integer status_Tumor_Routed;
	private Integer status_Tumor_Chosen;
	private Integer status_Tumor_Indexed;
	private Integer status_Tumor_Full_coded;
	private Integer status_Tumor_Rejected;	

}
