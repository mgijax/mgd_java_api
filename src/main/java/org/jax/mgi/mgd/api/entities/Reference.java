package org.jax.mgi.mgd.api.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;
import org.jax.mgi.mgd.api.util.Constants;

import io.swagger.annotations.ApiModel;

@Entity
@ApiModel(value = "Reference Model Object")
@Table(name="bib_refs")
public class Reference extends Base {

	@Id
	@Column(name="_Refs_key")
	public Long _refs_key;
	
	@Column(name="authors")
	public String authors;

	@Column(name="_primary")
	public String primaryAuthor;

	@Column(name="title")
	public String title;

	@Column(name="journal")
	public String journal;
	
	@Column(name="vol")
	public String volume;

	@Column(name="issue")
	public String issue;
	
	@Column(name="date")
	public String date;
	
	@Column(name="year")
	public String year;

	@Column(name="pgs")
	public String pages;
	
	@Column(name="abstract")
	public String refAbstract;		// just "abstract" is a Java reserved word, so need a prefix
	
	@Column(name="isReviewArticle")
	public int isReviewArticle;

	/* The @Fetch annotation (below) allows us to specify multiple EAGER-loaded collections, which would
	 * otherwise throw an error.
	 */
	@OneToMany (targetEntity=ReferenceWorkflowStatus.class, fetch=FetchType.EAGER)
	@JoinColumn(name="_refs_key", referencedColumnName="_refs_key")
	@Where(clause="iscurrent = 1")
	@Fetch(value=FetchMode.SUBSELECT)
	private List<ReferenceWorkflowStatus> currentWorkflowStatuses;

	@OneToMany (targetEntity=AccessionID.class, fetch=FetchType.EAGER)
	@JoinColumn(name="_object_key", referencedColumnName="_refs_key")
	@Where(clause="_mgitype_key = 1")
	@OrderBy("_logicaldb_key, preferred desc")
	private List<AccessionID> accessionIDs;

	@OneToOne (targetEntity=Term.class, fetch=FetchType.EAGER)
	@JoinColumn(name="_referencetype_key", referencedColumnName="_term_key")
	private Term referenceTypeTerm;
	
	/***--- transient methods ---***/
	
	/* Find and return the first accession ID mathcing any specified logical database, prefix,
	 * is-preferred, and is-private settings.
	 */
	@Transient
	public String findFirstID(Integer ldb, String prefix, Integer preferred, Integer isPrivate) {
		for (AccessionID accID : accessionIDs) {
			if ((ldb == null) || (ldb == accID._logicaldb_key))
				if ((prefix == null) || prefix.equals(accID.prefixPart))
					if ((preferred == null) || (preferred == accID.preferred))
						if ((isPrivate == null) || (isPrivate == accID.is_private))
							return accID.accID;
		}
		return null;
	}
	
	@Transient
	public String getJnumid() {
		return this.findFirstID(Constants.LDB_MGI, "J:", Constants.PREFERRED, Constants.PUBLIC);
	}
	
	@Transient
	public String getDoiid() {
		return this.findFirstID(Constants.LDB_DOI, null, null, null);
	}

	@Transient
	public String getPubmedid() {
		return this.findFirstID(Constants.LDB_PUBMED, null, null, null);
	}

	@Transient
	public String getMgiid() {
		return this.findFirstID(Constants.LDB_MGI, "MGI:", null, null);
	}

	@Transient
	public String getGorefid() {
		return this.findFirstID(Constants.LDB_GOREF, null, Constants.SECONDARY, Constants.PRIVATE);
	}

	@Transient
	public String getReferenceType() {
		if (referenceTypeTerm == null) { return null; }
		return referenceTypeTerm.term;
	}
	
	@Transient
	private String getStatus(String groupAbbrev) {
		for (ReferenceWorkflowStatus rws : this.currentWorkflowStatuses) {
			if (rws.isForGroup(groupAbbrev)) {
				return rws.getStatus();
			}
		}
		return null;
	}
	
	@Transient
	public String getGo_status() { return this.getStatus(Constants.WG_GO); }
	
	@Transient
	public String getAp_status() { return this.getStatus(Constants.WG_AP); }
	
	@Transient
	public String getGxd_status() { return this.getStatus(Constants.WG_GXD); }
	
	@Transient
	public String getQtl_status() { return this.getStatus(Constants.WG_QTL); }
	
	@Transient
	public String getTumor_status() { return this.getStatus(Constants.WG_TUMOR); }
	
	@Transient
	private int checkStatus(String statusFound, String statusDesired) {
		if (statusDesired.equals(statusFound)) { return 1; } else { return 0; }
	}
	
	@Transient
	public int getStatus_AP_Chosen() { return checkStatus(getAp_status(), Constants.WS_CHOSEN); }
	
	@Transient
	public int getStatus_AP_Fully_curated() { return checkStatus(getAp_status(), Constants.WS_CURATED); }
	
	@Transient
	public int getStatus_AP_Indexed() { return checkStatus(getAp_status(), Constants.WS_INDEXED); }
	
	@Transient
	public int getStatus_AP_Not_Routed() { return checkStatus(getAp_status(), Constants.WS_NOT_ROUTED); }
	
	@Transient
	public int getStatus_AP_Rejected() { return checkStatus(getAp_status(), Constants.WS_REJECTED); }
	
	@Transient
	public int getStatus_AP_Routed() { return checkStatus(getAp_status(), Constants.WS_ROUTED); }
	
	@Transient
	public int getStatus_GO_Chosen() { return checkStatus(getGo_status(), Constants.WS_CHOSEN); }
	
	@Transient
	public int getStatus_GO_Fully_curated() { return checkStatus(getGo_status(), Constants.WS_CURATED); }
	
	@Transient
	public int getStatus_GO_Indexed() { return checkStatus(getGo_status(), Constants.WS_INDEXED); }
	
	@Transient
	public int getStatus_GO_Not_Routed() { return checkStatus(getGo_status(), Constants.WS_NOT_ROUTED); }
	
	@Transient
	public int getStatus_GO_Rejected() { return checkStatus(getGo_status(), Constants.WS_REJECTED); }
	
	@Transient
	public int getStatus_GO_Routed() { return checkStatus(getGo_status(), Constants.WS_ROUTED); }
	
	@Transient
	public int getStatus_GXD_Chosen() { return checkStatus(getGxd_status(), Constants.WS_CHOSEN); }
	
	@Transient
	public int getStatus_GXD_Fully_curated() { return checkStatus(getGxd_status(), Constants.WS_CURATED); }
	
	@Transient
	public int getStatus_GXD_Indexed() { return checkStatus(getGxd_status(), Constants.WS_INDEXED); }
	
	@Transient
	public int getStatus_GXD_Not_Routed() { return checkStatus(getGxd_status(), Constants.WS_NOT_ROUTED); }
	
	@Transient
	public int getStatus_GXD_Rejected() { return checkStatus(getGxd_status(), Constants.WS_REJECTED); }
	
	@Transient
	public int getStatus_GXD_Routed() { return checkStatus(getGxd_status(), Constants.WS_ROUTED); }
	
	@Transient
	public int getStatus_QTL_Chosen() { return checkStatus(getQtl_status(), Constants.WS_CHOSEN); }
	
	@Transient
	public int getStatus_QTL_Fully_curated() { return checkStatus(getQtl_status(), Constants.WS_CURATED); }
	
	@Transient
	public int getStatus_QTL_Indexed() { return checkStatus(getQtl_status(), Constants.WS_INDEXED); }
	
	@Transient
	public int getStatus_QTL_Not_Routed() { return checkStatus(getQtl_status(), Constants.WS_NOT_ROUTED); }
	
	@Transient
	public int getStatus_QTL_Rejected() { return checkStatus(getQtl_status(), Constants.WS_REJECTED); }
	
	@Transient
	public int getStatus_QTL_Routed() { return checkStatus(getQtl_status(), Constants.WS_ROUTED); }
	
	@Transient
	public int getStatus_Tumor_Chosen() { return checkStatus(getTumor_status(), Constants.WS_CHOSEN); }
	
	@Transient
	public int getStatus_Tumor_Fully_curated() { return checkStatus(getTumor_status(), Constants.WS_CURATED); }
	
	@Transient
	public int getStatus_Tumor_Indexed() { return checkStatus(getTumor_status(), Constants.WS_INDEXED); }
	
	@Transient
	public int getStatus_Tumor_Not_Routed() { return checkStatus(getTumor_status(), Constants.WS_NOT_ROUTED); }
	
	@Transient
	public int getStatus_Tumor_Rejected() { return checkStatus(getTumor_status(), Constants.WS_REJECTED); }
	
	@Transient
	public int getStatus_Tumor_Routed() { return checkStatus(getTumor_status(), Constants.WS_ROUTED); }
	
	/* compute the short citation, so it's up-to-the-minute and doesn't rely on a cache table that could
	 * be out of date.
	 */
	@Transient
	public String getShort_citation() {
		StringBuffer sb = new StringBuffer();
		if (this.primaryAuthor != null) { sb.append(this.primaryAuthor); }
		sb.append(", ");
		if (this.journal != null) { sb.append(this.journal); }
		sb.append(" ");
		if (this.date != null) { sb.append(this.date); }
		sb.append(";");
		if (this.volume != null) { sb.append(this.volume); }
		sb.append("(");
		if (this.issue != null) { sb.append(this.issue); }
		sb.append("):");
		if (this.pages != null) { sb.append(this.pages); }
		return sb.toString();
	}
}
