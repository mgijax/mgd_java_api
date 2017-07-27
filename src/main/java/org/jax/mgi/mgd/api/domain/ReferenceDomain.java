package org.jax.mgi.mgd.api.domain;

import org.jax.mgi.mgd.api.entities.Reference;
import org.jboss.logging.Logger;

/* Is: a domain object that represents a single reference in mgd.
 * Has: fields needed to display/edit in the PWI, where those values for those fields are carried
 * 	back to the GXDI to be put into entity objects and persisted to the database
 * Does: serves as a data-transfer object between the GXDI and the PWI, allowing the model objects (in
 * 	the entities package) to be closer to the database and keeping the PWI's interactions as simple
 *	as possible
 */
public class ReferenceDomain {
	private Logger log = Logger.getLogger(getClass());

	public Long _refs_key;
	public String authors;
	public String primaryAuthor;
	public String title;
	public String journal;
	public String volume;
	public String issue;
	public String date;
	public String year;
	public String pages;
	public String isReviewArticle;
	public String jnumid;
	public String doiid;
	public String pubmedid;
	public String mgiid;
	public String gorefid;
	public String referenceType;
	public String ref_abstract;
	public String referencenote;
	public String short_citation;
	public String ap_status;
	public String go_status;
	public String gxd_status;
	public String qtl_status;
	public String tumor_status;
	public Integer status_AP_Chosen = 0;
	public Integer status_AP_Fully_curated = 0;
	public Integer status_AP_Indexed = 0;
	public Integer status_AP_Not_Routed = 0;
	public Integer status_AP_Rejected = 0;
	public Integer status_AP_Routed = 0;
	public Integer status_GO_Chosen = 0;
	public Integer status_GO_Fully_curated = 0;
	public Integer status_GO_Indexed = 0;
	public Integer status_GO_Not_Routed = 0;
	public Integer status_GO_Rejected = 0;
	public Integer status_GO_Routed = 0;
	public Integer status_GXD_Chosen = 0;
	public Integer status_GXD_Fully_curated = 0;
	public Integer status_GXD_Indexed = 0;
	public Integer status_GXD_Not_Routed = 0;
	public Integer status_GXD_Rejected = 0;
	public Integer status_GXD_Routed = 0;
	public Integer status_QTL_Chosen = 0;
	public Integer status_QTL_Fully_curated = 0;
	public Integer status_QTL_Indexed = 0;
	public Integer status_QTL_Not_Routed = 0;
	public Integer status_QTL_Rejected = 0;
	public Integer status_QTL_Routed = 0;
	public Integer status_Tumor_Chosen = 0;
	public Integer status_Tumor_Fully_curated = 0;
	public Integer status_Tumor_Indexed = 0;
	public Integer status_Tumor_Not_Routed = 0;
	public Integer status_Tumor_Rejected = 0;
	public Integer status_Tumor_Routed = 0;
	
	/***--- constructors ---***/
	
	/* empty construtor - ready for population from JSON */
	public ReferenceDomain() {}
	
	/* pull data from the Reference passed in, using it to populate this domain object for transfer to client
	 */
	public ReferenceDomain(Reference r) {
		log.info("in ReferenceDomain constructor");
		this._refs_key = r._refs_key;
		this.authors = r.authors;
		this.primaryAuthor = r.primaryAuthor;
		this.title = r.title;
		this.journal = r.journal;
		this.volume = r.volume;
		this.issue = r.issue;
		this.date = r.date;
		this.year = r.year;
		this.pages = r.pages;
		if (r.isReviewArticle == 0) {
			this.isReviewArticle = "No";
		} else {
			this.isReviewArticle = "Yes";
		}
		this.ref_abstract = r.ref_abstract;
		this.referencenote = r.getReferencenote();
		this.jnumid = r.getJnumid();
		this.doiid = r.getDoiid();
		this.pubmedid = r.getPubmedid();
		this.mgiid = r.getMgiid();
		this.gorefid = r.getGorefid();
		this.referenceType = r.getReferenceType();
		this.short_citation = r.getShort_citation();
		this.ap_status = r.getAp_status();
		this.go_status = r.getGo_status();
		this.gxd_status = r.getGxd_status();
		this.qtl_status = r.getQtl_status();
		this.tumor_status = r.getTumor_status();
		this.status_AP_Chosen = r.getStatus_AP_Chosen();
		this.status_AP_Fully_curated = r.getStatus_AP_Fully_curated();
		this.status_AP_Indexed = r.getStatus_AP_Indexed();
		this.status_AP_Not_Routed = r.getStatus_AP_Not_Routed();
		this.status_AP_Rejected = r.getStatus_AP_Rejected();
		this.status_AP_Routed = r.getStatus_AP_Routed();
		this.status_GO_Chosen = r.getStatus_GO_Chosen();
		this.status_GO_Fully_curated = r.getStatus_GO_Fully_curated();
		this.status_GO_Indexed = r.getStatus_GO_Indexed();
		this.status_GO_Not_Routed = r.getStatus_GO_Not_Routed();
		this.status_GO_Rejected = r.getStatus_GO_Rejected();
		this.status_GO_Routed = r.getStatus_GO_Routed();
		this.status_GXD_Chosen = r.getStatus_GXD_Chosen();
		this.status_GXD_Fully_curated = r.getStatus_GXD_Fully_curated();
		this.status_GXD_Indexed = r.getStatus_GXD_Indexed();
		this.status_GXD_Not_Routed = r.getStatus_GXD_Not_Routed();
		this.status_GXD_Rejected = r.getStatus_GXD_Rejected();
		this.status_GXD_Routed = r.getStatus_GXD_Routed();
		this.status_QTL_Chosen = r.getStatus_QTL_Chosen();
		this.status_QTL_Fully_curated = r.getStatus_QTL_Fully_curated();
		this.status_QTL_Indexed = r.getStatus_QTL_Indexed();
		this.status_QTL_Not_Routed = r.getStatus_QTL_Not_Routed();
		this.status_QTL_Rejected = r.getStatus_QTL_Rejected();
		this.status_QTL_Routed = r.getStatus_QTL_Routed();
		this.status_Tumor_Chosen = r.getStatus_Tumor_Chosen();
		this.status_Tumor_Fully_curated = r.getStatus_Tumor_Fully_curated();
		this.status_Tumor_Indexed = r.getStatus_Tumor_Indexed();
		this.status_Tumor_Not_Routed = r.getStatus_Tumor_Not_Routed();
		this.status_Tumor_Rejected = r.getStatus_Tumor_Rejected();
		this.status_Tumor_Routed = r.getStatus_Tumor_Routed();
	}
}
