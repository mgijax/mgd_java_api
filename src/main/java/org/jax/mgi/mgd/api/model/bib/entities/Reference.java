package org.jax.mgi.mgd.api.model.bib.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.jax.mgi.mgd.api.model.EntityBase;
import org.jax.mgi.mgd.api.model.acc.entities.Accession;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.entities.Term;
import org.jax.mgi.mgd.api.util.Constants;
import org.jboss.logging.Logger;

import lombok.Getter;
import lombok.Setter;
import io.swagger.annotations.ApiModel;

@Getter @Setter
@Entity
@ApiModel(value = "Reference Model Object")
@Table(name="bib_refs")
public class Reference extends EntityBase {

	@Transient
	private Logger log = Logger.getLogger(getClass());


	@Id
	@Column(name="_Refs_key")
	private Integer _refs_key;

	@Column(name="authors")
	private String authors;

	@Column(name="_primary")
	private String primary_author;

	@Column(name="title")
	private String title;

	@Column(name="journal")
	private String journal;

	@Column(name="vol")
	private String volume;

	@Column(name="issue")
	private String issue;

	@Column(name="date")
	private String date;

	@Column(name="year")
	private Integer year;

	@Column(name="pgs")
	private String pages;

	@Column(name="abstract")
	private String ref_abstract;		// just "abstract" is a Java reserved word, so need a prefix

	@Column(name="isReviewArticle")
	private int isReviewArticle;

	@Column(name="isDiscard")
	private int is_discard;

	@Column(name="creation_date")
	private Date creation_date;

	@Column(name="modification_date")
	private Date modification_date;

	@OneToOne (targetEntity=ReferenceStatusView.class, fetch=FetchType.EAGER)
	@JoinColumn(name="_refs_key", referencedColumnName="_refs_key", insertable=false, updatable=false)
	private ReferenceStatusView statusView;

	@OneToOne (targetEntity=User.class, fetch=FetchType.LAZY)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdByUser;

	@OneToOne (targetEntity=User.class, fetch=FetchType.LAZY)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedByUser;

	// maps workflow group abbrev to current status for that group, cached in memory for efficiency - not persisted
	@Transient
	private Map<String,String> workflowStatusCache;

	/* The @Fetch annotation (below) allows us to specify multiple EAGER-loaded collections, which would
	 * otherwise throw an error.
	 */
	@OneToMany (targetEntity=ReferenceWorkflowStatus.class, fetch=FetchType.LAZY)
	@JoinColumn(name="_refs_key", referencedColumnName="_refs_key")
	@Fetch(value=FetchMode.SUBSELECT)
	private List<ReferenceWorkflowStatus> workflowStatuses;

	@OneToMany (targetEntity=ReferenceWorkflowTag.class, fetch=FetchType.LAZY)
	@JoinColumn(name="_refs_key", referencedColumnName="_refs_key")
	@Fetch(value=FetchMode.SUBSELECT)
	private List<ReferenceWorkflowTag> workflowTags;

	@OneToMany (targetEntity=Accession.class, fetch=FetchType.LAZY)
	@JoinColumn(name="_object_key", referencedColumnName="_refs_key")
	@Where(clause="_mgitype_key = 1")
	@OrderBy("_logicaldb_key, preferred desc")
	private List<Accession> accessionIDs;

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

	@OneToOne (targetEntity=Term.class, fetch=FetchType.LAZY)
	@JoinColumn(name="_referencetype_key", referencedColumnName="_term_key")
	private Term referenceTypeTerm;

	// one to many, because notes might not exist (leaving it 1-0)
	@OneToMany (targetEntity=ReferenceNote.class, fetch=FetchType.LAZY)
	@JoinColumn(name="_refs_key", referencedColumnName="_refs_key")
	@Fetch(value=FetchMode.SUBSELECT)
	private List<ReferenceNote> notes;

	// one to many, because row in citation cache might not exist (leaving it 1-0)
	@OneToMany (targetEntity=ReferenceCitationData.class, fetch=FetchType.EAGER)
	@JoinColumn(name="_refs_key", referencedColumnName="_refs_key")
	@Fetch(value=FetchMode.SUBSELECT)
	private List<ReferenceCitationData> citationData;

	// one to many, because book data most often does not exist (leaving it 1-0)
	@OneToMany (targetEntity=ReferenceBook.class, fetch=FetchType.LAZY)
	@JoinColumn(name="_refs_key", referencedColumnName="_refs_key")
	@Fetch(value=FetchMode.SUBSELECT)
	private List<ReferenceBook> bookList;

	// one to one, because counts will always exist
	@OneToOne (targetEntity=ReferenceAssociatedData.class, fetch=FetchType.LAZY)
	@JoinColumn(name="_refs_key", referencedColumnName="_refs_key")
	private ReferenceAssociatedData associatedData;

	// one to many, in case record is missing (leaving it 1-0)
	@OneToMany (targetEntity=ReferenceWorkflowData.class, fetch=FetchType.EAGER)
	@JoinColumn(name="_refs_key", referencedColumnName="_refs_key")
//	@Fetch(value=FetchMode.SUBSELECT)
	private Set<ReferenceWorkflowData> workflowData;

	/***--- transient methods ---***/

	/* Find and return the first accession ID matching any specified logical database, prefix,
	 * is-preferred, and is-private settings.
	 */
	@Transient
	private String findFirstID(Integer ldb, String prefix, Integer preferred, Integer isPrivate) {
		for (int i = 0; i < this.accessionIDs.size(); i++) {
			Accession accID = this.accessionIDs.get(i);
			if ((ldb == null) || (ldb.equals(accID.get_logicaldb_key()))) {
				if ((prefix == null) || prefix.equals(accID.getPrefixPart())) {
					if ((preferred == null) || (preferred.equals(accID.getPreferred()))) {
						if ((isPrivate == null) || (isPrivate.equals(accID.getIs_private()))) {
							return accID.getAccID();
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
			return rn.get(0).getNote();
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
		return referenceTypeTerm.getTerm();
	}

	@Transient
	public List<String> getWorkflowTagsAsStrings() {
		List<String> tags = new ArrayList<String>();
		for (ReferenceWorkflowTag rwTag : this.workflowTags) {
			tags.add(rwTag.getTag().getTerm());
		}
		Collections.sort(tags);
		return tags;
	}

	@Transient
	public void clearWorkflowStatusCache() {
		workflowStatusCache = null;
	}

	@Transient
	private void buildWorkflowStatusCache() {
		workflowStatusCache = new HashMap<String,String>();
		for (ReferenceWorkflowStatus rws : this.workflowStatuses) {
			if (rws.getIsCurrent() == 1) {
				workflowStatusCache.put(rws.getGroupAbbreviation(), rws.getStatus());
			}
		}
	}

	@Transient
	public String getStatus(String groupAbbrev) {
		if (workflowStatusCache == null) { this.buildWorkflowStatusCache(); }
		if (workflowStatusCache.containsKey(groupAbbrev)) {
			return workflowStatusCache.get(groupAbbrev);
		}
		return null;
	}

	@Transient
	public String getShort_citation() {
		if ((this.citationData != null) && (this.citationData.size() > 0)) {
			return this.citationData.get(0).getShort_citation();
		}
		return null;
	}

	@Transient
	public String getCachedID(String provider) {
		if ((this.citationData == null) || (this.citationData.size() == 0)) { return null; }
		if ("MGI".equals(provider)) {
			return this.citationData.get(0).getMgiid();
		} else if ("J:".equals(provider)) {
			return this.citationData.get(0).getJnumid();
		} else if ("DOI".equals(provider)) {
			return this.citationData.get(0).getDoiid();
		} else {
			return this.citationData.get(0).getPubmedid();
		}
	}

	/* set the reference's modification date to be 'now' and modified-by user to be 'currentUser'
	 */
	public void setModificationInfo(User currentUser) {
		this.modification_date = new Date();
		this.modifiedByUser = currentUser;
	}

	/* If this reference is of type Book, return an object with the extra book-related data (if one exists);
	 * otherwise return null.
	 */
	@Transient
	public ReferenceBook getBookData() {
		if ("Book".equals(this.referenceTypeTerm.getTerm()) && (this.bookList.size() > 0)) {
			return this.bookList.get(0);
		}
		return null;
	}

	/* set the given workflow data object to be the one for this reference
	 */
	@Transient
	public void setWorkflowData(ReferenceWorkflowData rwd) {
		if (this.workflowData.size() > 0) {
			this.workflowData.remove(0);
		}
		this.workflowData.add(rwd);
	}

	/* If this reference has workflow data, return an object with the extra workflow data;
	 * otherwise return null.
	 */
	@Transient
	public ReferenceWorkflowData getWorkflowData() {
		for(ReferenceWorkflowData data: workflowData) {
			return data;
		}
		return null;
	}
}
