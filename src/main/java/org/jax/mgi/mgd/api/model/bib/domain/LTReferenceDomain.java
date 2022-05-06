package org.jax.mgi.mgd.api.model.bib.domain;

import java.util.List;

import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.MGIReferenceAlleleAssocDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.MGIReferenceMarkerAssocDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.MGIReferenceStrainAssocDomain;

import lombok.Getter;
import lombok.Setter;


/* Is: a domain object that represents a single reference in mgd.
 * Has: fields needed to display/edit in the PWI, where those values for those fields are carried
 * 	back to the API to be put into entity objects and persisted to the database
 * Does: serves as a data-transfer object between the API and the PWI, allowing the model objects (in
 * 	the entities package) to be closer to the database and keeping the PWI's interactions as simple
 *	as possible
 */
@Getter @Setter
public class LTReferenceDomain extends BaseDomain {
	
	public String refsKey;
	public String authors;
	public String primary_author;
	public String title;
	public String journal;
	public String vol;
	public String issue;
	public String date;
	public String year;
	public String pgs;
	public String referenceAbstract;
	public String isReviewArticle;
	public String referenceTypeKey;
	public String referenceType;
	public String short_citation;
	public String createdBy;
	public String modifiedBy;
	public String creation_date;
	public String modification_date;
	
	// accession ids
	public String mgiid;	
	public String jnumid;
	public String doiid;
	public String pubmedid;
	public String gorefid;
	
	private ReferenceBookDomain referenceBook;
	private ReferenceNoteDomain referenceNote;
	private ReferenceWorkflowDataDomain workflowData;
	
	// workflow status
	public String ap_status;
	public String go_status;
	public String gxd_status;
	public String pro_status;
	public String qtl_status;
	public String tumor_status;
	public List<ReferenceWorkflowStatusDomain> statusHistory;

	// workflow relevance/current relevance
	public String editRelevance;
	public String editRelevanceKey;
	public List<ReferenceWorkflowRelevanceDomain> relevanceHistory;

	public List<String> workflow_tags;	
	public List<ReferenceWorkflowTagDomain> tagHistory;	
	
	public List<String> associated_data;
	
	// PWI loads this data via calls to MGIReferenceAssocService/getXXX()
	public List<MGIReferenceAlleleAssocDomain> alleleAssocs;
	public List<MGIReferenceStrainAssocDomain> strainAssocs;
	public List<MGIReferenceMarkerAssocDomain> markerAssocs;

	/***--- constructors ---***/
	
//	/* empty constructor - ready for population from JSON */
//	public LTReferenceDomain() {}
	
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
