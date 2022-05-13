package org.jax.mgi.mgd.api.model.bib.domain;

import java.util.List;

import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.MGIReferenceAlleleAssocDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.MGIReferenceMarkerAssocDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.MGIReferenceStrainAssocDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ReferenceDomain extends BaseDomain {
	
	// used by ReferenceController/ReferenceService/get() method
	// similar to but not exactly the same as ReferenceSearchDomain/search() method
	
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
	
	// workflow status
	private String ap_status;
	private String go_status;
	private String gxd_status;
	private String pro_status;
	private String qtl_status;
	private String tumor_status;
	private List<ReferenceWorkflowStatusDomain> statusCurrent;
	private List<ReferenceWorkflowStatusDomain> statusHistory;

	// workflow relevance/current relevance
	private String editRelevance;
	private String editRelevanceKey;
	private List<ReferenceWorkflowRelevanceDomain> relevanceHistory;

	private List<String> workflowTagString;	
	private List<ReferenceWorkflowTagDomain> workflowTags;	
	
	private List<String> associated_data;
	
	// PWI loads this data via calls to MGIReferenceAssocService/getXXX()
	private List<MGIReferenceAlleleAssocDomain> alleleAssocs;
	private List<MGIReferenceStrainAssocDomain> strainAssocs;
	private List<MGIReferenceMarkerAssocDomain> markerAssocs;	


	/* update the status for the given workflow group within this domain object (This method does not persist the
	 * change to the database.)
	 */
	public void setStatus (String group, String status) throws APIException {
		if (group.equalsIgnoreCase("AP")) {
			this.ap_status = status;
		} else if (group.equalsIgnoreCase("GO")) {
			this.go_status = status;
		} else if (group.equalsIgnoreCase("GXD")) {
			this.gxd_status = status;
		} else if (group.equalsIgnoreCase("PRO")) {
			this.pro_status = status;
		} else if (group.equalsIgnoreCase("QTL")) {
			this.qtl_status = status;
		} else if (group.equalsIgnoreCase("Tumor")) {
			this.tumor_status = status;
		} else {
			throw new APIException("Unknown workflow group: " + group);
		}	
	}
}
