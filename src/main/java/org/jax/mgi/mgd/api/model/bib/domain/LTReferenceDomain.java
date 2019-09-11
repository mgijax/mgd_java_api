package org.jax.mgi.mgd.api.model.bib.domain;

import java.util.List;

import org.jax.mgi.mgd.api.exception.APIException;
import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.MGIReferenceAssocDomain;

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
	
	// books
	public String book_author;
	public String book_title;
	public String place;
	public String publisher;
	public String series_ed;
	
	// accession ids
	public String mgiid;	
	public String jnumid;
	public String doiid;
	public String pubmedid;
	public String gorefid;
	
	public String referenceType;
	public String referenceTypeKey;
	public String referenceAbstract;
	public String referenceNote;
	public String short_citation;

	public String isReviewArticle;	
	public String isDiscard;

	// workflow status
	public String ap_status;
	public String go_status;
	public String gxd_status;
	public String qtl_status;
	public String tumor_status;
	
	// workflow data
	public String has_pdf;
	public String has_supplemental;
	public String link_to_supplemental;
	public String has_extracted_text;
	
	public String created_by;
	public String modified_by;
	public String creation_date;
	public String modification_date;
	
	public List<String> workflow_tags;	
	public List<String> associated_data;
	public List<LTReferenceWorkflowStatusDomain> statusHistory;
	
	// PWI loads this data via call to MGIReferenceAssocService/getAlleles()
	public List<MGIReferenceAssocDomain> alleleAssocs;

	/***--- constructors ---***/
	
	/* empty constructor - ready for population from JSON */
	public LTReferenceDomain() {}
	
	/* add the given status history to this domain object (not here by default, but added for detail pages)
	 */
	public void setStatusHistory (List<LTReferenceWorkflowStatusDomain> history) {
		this.statusHistory = history;
	}
	
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
		} else if (group.equalsIgnoreCase("QTL")) {
			this.qtl_status = status;
		} else if (group.equalsIgnoreCase("Tumor")) {
			this.tumor_status = status;
		} else {
			throw new APIException("Unknown workflow group: " + group);
		}
	}
}
