package org.jax.mgi.mgd.api.entities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Singleton;
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

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;
import org.jax.mgi.mgd.api.dao.ReferenceDAO;
import org.jax.mgi.mgd.api.domain.ReferenceDomain;
import org.jax.mgi.mgd.api.util.Constants;
import org.jboss.logging.Logger;

import io.swagger.annotations.ApiModel;

@Singleton
@Entity
@ApiModel(value = "Reference Model Object")
@Table(name="bib_refs")
public class Reference extends Base {
	
	@Transient
	private Logger log = Logger.getLogger(getClass());

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
	public String ref_abstract;		// just "abstract" is a Java reserved word, so need a prefix
	
	@Column(name="isReviewArticle")
	public int isReviewArticle;

	// maps workflow group abbrev to current status for that group, cached in memory for efficiency - not persisted
	@Transient
	private Map<String,String> workflowStatusCache;
	
	/* The @Fetch annotation (below) allows us to specify multiple EAGER-loaded collections, which would
	 * otherwise throw an error.
	 */
	@OneToMany (targetEntity=ReferenceWorkflowStatus.class, fetch=FetchType.EAGER)
	@JoinColumn(name="_refs_key", referencedColumnName="_refs_key")
	@Fetch(value=FetchMode.SUBSELECT)
	private List<ReferenceWorkflowStatus> workflowStatuses;

	@OneToMany (targetEntity=AccessionID.class, fetch=FetchType.EAGER)
	@JoinColumn(name="_object_key", referencedColumnName="_refs_key")
	@Where(clause="_mgitype_key = 1")
	@OrderBy("_logicaldb_key, preferred desc")
	private List<AccessionID> accessionIDs;

	@OneToMany (targetEntity=ReferenceAlleleAssociation.class, fetch=FetchType.LAZY)
	@JoinColumn(name="_refs_key", referencedColumnName="_refs_key")
	@BatchSize(size=200)
	@Where(clause="_mgitype_key = 11")
	@Fetch(value=FetchMode.SUBSELECT)
	private List<ReferenceAlleleAssociation> alleleAssociations;

	@OneToMany (targetEntity=ReferenceMarkerAssociation.class, fetch=FetchType.LAZY)
	@JoinColumn(name="_refs_key", referencedColumnName="_refs_key")
	@BatchSize(size=200)
	@Fetch(value=FetchMode.SUBSELECT)
	private List<ReferenceMarkerAssociation> markerAssociations;

	@OneToOne (targetEntity=Term.class, fetch=FetchType.EAGER)
	@JoinColumn(name="_referencetype_key", referencedColumnName="_term_key")
	private Term referenceTypeTerm;
	
	// cannot eager-load an attribute that might not be there
	@OneToMany (targetEntity=ReferenceNote.class, fetch=FetchType.EAGER)
	@JoinColumn(name="_refs_key", referencedColumnName="_refs_key")
	@Fetch(value=FetchMode.SUBSELECT)
	private List<ReferenceNote> notes;

	/***--- transient methods ---***/
	
	/* Find and return the first accession ID matching any specified logical database, prefix,
	 * is-preferred, and is-private settings.
	 */
	@Transient
	private String findFirstID(Integer ldb, String prefix, Integer preferred, Integer isPrivate) {
		for (int i = 0; i < this.accessionIDs.size(); i++) {
			AccessionID accID = this.accessionIDs.get(i);
			if ((ldb == null) || (ldb.equals(accID._logicaldb_key))) {
				if ((prefix == null) || prefix.equals(accID.prefixPart)) {
					if ((preferred == null) || (preferred.equals(accID.preferred))) {
						if ((isPrivate == null) || (isPrivate.equals(accID.is_private))) {
							return accID.accID;
						}
					}
				}
			}
		}
		return null;
	}
	
	@Transient
	public String getReferencenote() {
		List<ReferenceNote> rn = this.notes;
		if ((rn != null) && (rn.size() > 0)) {
			return rn.get(0).note;
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
		return this.findFirstID(Constants.LDB_GOREF, null, null, null);
	}

	@Transient
	public String getReferenceType() {
		if (referenceTypeTerm == null) { return null; }
		return referenceTypeTerm.term;
	}
	
	@Transient
	private void buildWorkflowStatusCache() {
		workflowStatusCache = new HashMap<String,String>();
		for (ReferenceWorkflowStatus rws : this.workflowStatuses) {
			if (rws.isCurrent == 1) {
				workflowStatusCache.put(rws.getGroupAbbreviation(), rws.getStatus());
			}
		}
	}
	
	@Transient
	private String getStatus(String groupAbbrev) {
		if (workflowStatusCache == null) { this.buildWorkflowStatusCache(); }
		if (workflowStatusCache.containsKey(groupAbbrev)) {
			return workflowStatusCache.get(groupAbbrev);
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
	
	/* convenience method, used by applyDomainChanges() to reduce redundant code in setting workflow
	 * group statuses.  returns true if an update was made, false if no change.
	 */
	private boolean updateStatus(String groupAbbrev, String currentStatus, String newStatus, ReferenceDAO refDAO) {
		// no update if new status matches old status (or if no group is specified)
		if ( ((currentStatus != null) && currentStatus.equals(newStatus)) || (groupAbbrev == null) ||
				((currentStatus == null) && (newStatus == null)) ) {
			return false;
		}
		
		// At this point, we know we have a status update.  If there was an existing record, we need
		// to flag it as not current.
		if (currentStatus != null) {
			for (ReferenceWorkflowStatus rws : this.workflowStatuses) {
				if ( (rws.isCurrent == 1) && groupAbbrev.equals(rws.getGroupAbbreviation()) ) {
					rws.isCurrent = 0;
					break;				// no more can match, so exit the loop
				}
			}
		}
		
		// Now we need to add a new status record for this change.
		
		ReferenceWorkflowStatus newRws = new ReferenceWorkflowStatus();
		newRws._assoc_key = refDAO.getNextWorkflowStatusKey();
		newRws._refs_key = this._refs_key;
		newRws.isCurrent = 1;
		newRws.groupTerm = refDAO.getTermByAbbreviation(Constants.VOC_WORKFLOW_GROUP, groupAbbrev);
		newRws.statusTerm = refDAO.getTermByTerm(Constants.VOC_WORKFLOW_STATUS, newStatus);
		newRws.createdByUser = refDAO.getUser("mgd_dbo");
		newRws.modifiedByUser = newRws.createdByUser;

		this.workflowStatuses.add(newRws);
		return true;
	}
	
	/* take the data from the domain object and overwrite any changed data into this object
	 * (does not automatically persist it into the database -- just applies it to the object in memory)
	 */
	@Transient
	public void applyDomainChanges(ReferenceDomain rd, ReferenceDAO refDAO) {
		/* At the moment, the only thing we're saving are status changes for workflow groups.
		 * 	1. Any status values that are the same as what's in the object don't need to be updated.
		 * 	2. A workflow group with a different status value than what's in this object will need:
		 * 		a. the old status to be changed so isCurrent = 0, and
		 * 		b. a new ReferenceWorkflowStatus object created and set to be current
		 * 			i. How to deal with assigning a key on this object to avoid concurrency issues?
		 * 				- keep highest in memory as static variable?  (synchronize access)
		 */
		boolean anyChanges = updateStatus("AP", this.getAp_status(), rd.ap_status, refDAO);
		anyChanges = anyChanges || updateStatus("GO", this.getGo_status(), rd.go_status, refDAO);
		anyChanges = anyChanges || updateStatus("GXD", this.getGxd_status(), rd.gxd_status, refDAO);
		anyChanges = anyChanges || updateStatus("QTL", this.getQtl_status(), rd.qtl_status, refDAO);
		anyChanges = anyChanges || updateStatus("Tumor", this.getTumor_status(), rd.tumor_status, refDAO);
		
		if (anyChanges) {
			this.workflowStatusCache = null;		// clear cache of old workflow statuses
		}
	}
}
