package org.jax.mgi.mgd.api.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	private Long _refs_key;
	
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

	@OneToOne (targetEntity=User.class, fetch=FetchType.EAGER)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdByUser;
	
	@OneToOne (targetEntity=User.class, fetch=FetchType.EAGER)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedByUser;

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

	@OneToMany (targetEntity=ReferenceWorkflowTag.class, fetch=FetchType.EAGER)
	@JoinColumn(name="_refs_key", referencedColumnName="_refs_key")
	@Fetch(value=FetchMode.SUBSELECT)
	private List<ReferenceWorkflowTag> workflowTags;

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
	
	// one to many, because notes might not exist (leaving it 1-0)
	@OneToMany (targetEntity=ReferenceNote.class, fetch=FetchType.EAGER)
	@JoinColumn(name="_refs_key", referencedColumnName="_refs_key")
	@Fetch(value=FetchMode.SUBSELECT)
	private List<ReferenceNote> notes;

	// one to many, because row in citation cache might not exist (leaving it 1-0)
	@OneToMany (targetEntity=ReferenceCitationData.class, fetch=FetchType.EAGER)
	@JoinColumn(name="_refs_key", referencedColumnName="_refs_key")
	@Fetch(value=FetchMode.SUBSELECT)
	private List<ReferenceCitationData> citationData;

	// one to many, because book data most often does not exist (leaving it 1-0)
	@OneToMany (targetEntity=ReferenceBook.class, fetch=FetchType.EAGER)
	@JoinColumn(name="_refs_key", referencedColumnName="_refs_key")
	@Fetch(value=FetchMode.SUBSELECT)
	private List<ReferenceBook> bookList;

	// one to one, because counts will always exist
	@OneToOne (targetEntity=ReferenceAssociatedData.class, fetch=FetchType.EAGER)
	@JoinColumn(name="_refs_key", referencedColumnName="_refs_key")
	private ReferenceAssociatedData associatedData;

	// one to many, in case record is missing (leaving it 1-0)
	@OneToMany (targetEntity=ReferenceWorkflowData.class, fetch=FetchType.EAGER)
	@JoinColumn(name="_refs_key", referencedColumnName="_refs_key")
	@Fetch(value=FetchMode.SUBSELECT)
	private List<ReferenceWorkflowData> workflowData;

	/***--- transient methods ---***/
	
	/* Find and return the first accession ID matching any specified logical database, prefix,
	 * is-preferred, and is-private settings.
	 */
	@Transient
	private String findFirstID(Integer ldb, String prefix, Integer preferred, Integer isPrivate) {
		for (int i = 0; i < this.accessionIDs.size(); i++) {
			AccessionID accID = this.accessionIDs.get(i);
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
	public List<String> getWorkflowTags() {
		List<String> tags = new ArrayList<String>();
		for (ReferenceWorkflowTag rwTag : this.workflowTags) {
			tags.add(rwTag.getTag().getTerm());
		}
		Collections.sort(tags);
		return tags;
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
	public String getShort_citation() {
		if ((this.citationData != null) && (this.citationData.size() > 0)) {
			return this.citationData.get(0).getShort_citation();
		}
		return null;
	}
	
	/* convenience method, used by applyStatusChanges() to reduce redundant code in setting workflow
	 * group statuses.  returns true if an update was made, false if no change.  persists any changes
	 * to the database.
	 */
	private boolean updateStatus(String groupAbbrev, String currentStatus, String newStatus, ReferenceDAO refDAO, User currentUser) {
		// no update if new status matches old status (or if no group is specified)
		if ( ((currentStatus != null) && currentStatus.equals(newStatus)) || (groupAbbrev == null) ||
				((currentStatus == null) && (newStatus == null)) ) {
			return false;
		}
		
		// At this point, we know we have a status update.  If there was an existing record, we need
		// to flag it as not current.
		if (currentStatus != null) {
			for (ReferenceWorkflowStatus rws : this.workflowStatuses) {
				if ( (rws.getIsCurrent() == 1) && groupAbbrev.equals(rws.getGroupAbbreviation()) ) {
					rws.setIsCurrent(0);
					break;				// no more can match, so exit the loop
				}
			}
		}
		
		// Now we need to add a new status record for this change -- and need to persist this new object to the
		// database explicitly, before the whole reference gets persisted later on.
		
		ReferenceWorkflowStatus newRws = new ReferenceWorkflowStatus();
		newRws.set_assoc_key(refDAO.getNextWorkflowStatusKey());
		newRws.set_refs_key(this._refs_key);
		newRws.setIsCurrent(1);
		newRws.setGroupTerm(refDAO.getTermByAbbreviation(Constants.VOC_WORKFLOW_GROUP, groupAbbrev));
		newRws.setStatusTerm(refDAO.getTermByTerm(Constants.VOC_WORKFLOW_STATUS, newStatus));
		newRws.setCreatedByUser(currentUser);
		newRws.setModifiedByUser(newRws.getCreatedByUser());
		newRws.setCreation_date(new Date());
		newRws.setModification_date(newRws.getCreation_date());
		refDAO.persist(newRws);

		this.workflowStatuses.add(newRws);
		return true;
	}
	
	/* handle applying any status changes for workflow groups.  If a group in 'rd' has a different status
	 * from what's in this Reference, we need:
	 * 		a. the old status to be changed so isCurrent = 0, and
	 * 		b. a new ReferenceWorkflowStatus object created and set to be current
	 * As well, if this Reference has no J: number and we just assigned a status other than "Not Routed", 
	 * then we assign the next available J: number to this reference.
	 */
	private boolean applyStatusChanges(ReferenceDomain rd, ReferenceDAO refDAO, User currentUser) throws Exception {
		// note that we need to put 'anyChanges' last for each OR pair, otherwise short-circuit evaluation
		// will only let the first change go through and the rest will not execute.
		
		boolean anyChanges = updateStatus(Constants.WG_AP, this.getAp_status(), rd.ap_status, refDAO, currentUser);
		anyChanges = updateStatus(Constants.WG_GO, this.getGo_status(), rd.go_status, refDAO, currentUser) || anyChanges;
		anyChanges = updateStatus(Constants.WG_GXD, this.getGxd_status(), rd.gxd_status, refDAO, currentUser) || anyChanges;
		anyChanges = updateStatus(Constants.WG_QTL, this.getQtl_status(), rd.qtl_status, refDAO, currentUser) || anyChanges;
		anyChanges = updateStatus(Constants.WG_TUMOR, this.getTumor_status(), rd.tumor_status, refDAO, currentUser) || anyChanges;
		
		if (anyChanges) {
			this.workflowStatusCache = null;		// clear cache of old workflow statuses
			
			// if we had a status change, if at least one status is not "Not Routed", and if the reference
			// doesn't already have a J#, we need to create one
			
			if (this.getJnumid() == null) {
				boolean anyNotRouted = false;
				for (String workgroup : Constants.WG_ALL) {
					String wgStatus = this.getStatus(workgroup);
					if ((wgStatus != null) && !wgStatus.equals(Constants.WS_NOT_ROUTED)) {
						anyNotRouted = true;
						break;
					}
				}
				
				if (anyNotRouted) {
					try {
						refDAO.assignNewJnumID(this._refs_key, currentUser.get_user_key());
					} catch (Exception e) {
						throw new Exception("Failed to assign J: number");
					}
				}
			} // if no J#
		}
		return anyChanges;
	}
	
	/* handle removing/adding any workflow tags that have changed between this Reference and the passed-in
	 * ReferenceDomain.  Persists any tag changes to the database.  Returns true if any changes were made,
	 * false otherwise.
	 */
	private boolean applyTagChanges(ReferenceDomain rd, ReferenceDAO refDAO, User currentUser) throws Exception {
		// short-circuit method if no tags in Reference or in ReferenceDomain
		if ((this.workflowTags.size() == 0) && (rd.workflow_tags.size() == 0)) {
			return false;
		}
		
		// set of tags specified in domain object (lowercased) -- potentially to add to object
		Set<String> toAdd = new HashSet<String>();
		for (String rdTag : rd.workflow_tags) {
			toAdd.add(rdTag.toLowerCase().trim());
		}

		// list of tags that need to be removed from this object
		List<ReferenceWorkflowTag> toDelete = new ArrayList<ReferenceWorkflowTag>();
		
		// Now we need to diff the set of tags we already have and the set of tags to potentially add. Anything
		// left in toAdd will need to be added as a new tag, and anything in toDelete will need to be removed.
		
		for (ReferenceWorkflowTag refTag : this.workflowTags) {
			String lowerTag = refTag.getTag().getTerm().toLowerCase();

			// matching tags
			if (toAdd.contains(lowerTag)) {
				// already have this one, don't need to add it
				toAdd.remove(lowerTag);
			} else {
				// current one isn't in the new list from domain object, so need to remove it
				toDelete.add(refTag);
			}
		}
		
		// remove defunct tags
		
		for (ReferenceWorkflowTag rwTag : toDelete) {
			// would like to do this here, but it fails due to a null _refs_key in the table:
			//		this.workflowTags.remove(rwTag);
			refDAO.remove(rwTag);
		}
		
		// add new tags (use shared method, as this will be useful when adding tags to batches of references)

		for (String rdTag : toAdd) {
			this.addTag(rdTag, refDAO, currentUser);
		}
		
		return (toDelete.size() > 0) || (toAdd.size() > 0);
	}
	
	/* set the reference's modification date to be 'now' and modified-by user to be 'currentUser'
	 */
	public void setModificationInfo(User currentUser) {
		this.modification_date = new Date();
		this.modifiedByUser = currentUser; 
	}
	
	/* method for removing a workflow tag for this Reference (no-op if this ref doesn't have the tag)
	 */
	@Transient
	public void removeTag(String rdTag, ReferenceDAO refDAO, User currentUser) throws Exception {
		if (this.workflowTags == null) { return; }
		
		String lowerTag = rdTag.toLowerCase().trim();
		for (ReferenceWorkflowTag refTag : this.workflowTags) {
			if (lowerTag.equals(refTag.getTag().getTerm().toLowerCase()) ) {
				refDAO.remove(refTag);
				this.setModificationInfo(currentUser);
				return;
			}
		}
	}
	
	/* shared method for adding a workflow tag to this Reference
	 */
	@Transient
	public void addTag(String rdTag, ReferenceDAO refDAO, User currentUser) throws Exception {
		// if we already have this tag applied, skip it (extra check needed for batch additions to avoid
		// adding duplicates)

		String lowerTag = rdTag.toLowerCase().trim();
		for (ReferenceWorkflowTag refTag : this.workflowTags) {
			if (lowerTag.equals(refTag.getTag().getTerm().toLowerCase()) ) {
				return;
			}
		}
		 
		// need to find the term of the tag, wrap it in an association, perist the association, and
		// add it to the workflow tags for this Reference
		
		Term tagTerm = refDAO.getTermByTerm(Constants.VOC_WORKFLOW_TAGS, rdTag);
		if (tagTerm != null) {
			if (!this.workflowTags.contains(tagTerm)) {
				ReferenceWorkflowTag rwTag = new ReferenceWorkflowTag();
				rwTag.set_assoc_key(refDAO.getNextWorkflowTagKey());
				rwTag.set_refs_key(this._refs_key);
				rwTag.setTag(tagTerm);
				rwTag.setCreatedByUser(currentUser);
				rwTag.setModifiedByUser(rwTag.getCreatedByUser());
				rwTag.setCreation_date(new Date());
				rwTag.setModification_date(rwTag.getCreation_date());
				refDAO.persist(rwTag);
				
				this.workflowTags.add(rwTag);
				this.setModificationInfo(currentUser);
			}
		}
	}
	
	/* comparison function that handles null values well
	 */
	private boolean smartEqual(Object a, Object b) {
		if (a == null) {
			if (b == null) { return true; }
			else { return false; }
		}
		return a.equals(b);
	}
	
	private boolean applyWorkflowDataChanges(ReferenceDomain rd, ReferenceDAO refDAO, User currentUser) {
		// at most one set of workflow data per reference
		// need to handle:  updated workflow data, new workflow data -- (no deletions)
		
		boolean anyChanges = false;
		ReferenceWorkflowData myWD = this.getWorkflowData();
		
		if (myWD != null) {
			// Compare fields and update if needed.  We do not update the extracted text and the
			// has-PDF flag, as those are updated by other processes.
			
			if (!smartEqual(myWD.getSupplemental(), rd.has_supplemental)
				|| !smartEqual(myWD.getLink_supplemental(), rd.link_to_supplemental)) {
				
				myWD.setSupplementalTerm(refDAO.getTermByTerm(Constants.VOC_SUPPLEMENTAL, rd.has_supplemental));
				myWD.setLink_supplemental(rd.link_to_supplemental);
				myWD.setModifiedByUser(currentUser);
				myWD.setModification_date(new Date());
				anyChanges = true;
			}
			
		} else {
			// For some reason, no workflow data record exists.  So, create one.

			myWD = new ReferenceWorkflowData();
			myWD.set_refs_key(this._refs_key);
			myWD.setHas_pdf(0);
			myWD.setSupplementalTerm(refDAO.getTermByTerm(Constants.VOC_SUPPLEMENTAL, rd.has_supplemental));
			myWD.setLink_supplemental(rd.link_to_supplemental);
			myWD.setExtracted_text(null);
			myWD.setCreatedByUser(currentUser);
			myWD.setModifiedByUser(this.createdByUser);
			myWD.setCreation_date(new Date());
			myWD.setModification_date(myWD.getCreation_date()); 
			
			refDAO.persist(myWD);
			this.workflowData.add(myWD);
			anyChanges = true;
		}

		return anyChanges;
	}
	
	private boolean applyBookChanges(ReferenceDomain rd, ReferenceDAO refDAO, User currentUser) {
		// at most one set of book data per reference
		// need to handle:  deleted book data, updated book data, new book data

		boolean anyChanges = false;
		boolean wasBook = "Book".equalsIgnoreCase(this.getReferenceType());
		boolean willBeBook = "Book".equalsIgnoreCase(rd.reference_type);
		
		// If this reference is already a book and will continue to be a book, need to apply
		// any changes to the fields of the existing book data.
		if (wasBook && willBeBook && (this.bookList.size() > 0)) {
			ReferenceBook book = this.bookList.get(0);

			if (!smartEqual(book.getBook_author(), rd.book_author) || !smartEqual(book.getBook_title(), rd.book_title) || 
				!smartEqual(book.getPlace(), rd.place) || !smartEqual(book.getPublisher(), rd.publisher) ||
				!smartEqual(book.getSeries_edition(), rd.series_edition)) {

				book.setBook_author(rd.book_author);
				book.setBook_title(rd.book_title);
				book.setPlace(rd.place);
				book.setPublisher(rd.publisher);
				book.setSeries_edition(rd.series_edition);
				book.setModification_date(new Date());
				anyChanges = true;
			}
			
		} else if (wasBook && (this.bookList.size() > 0)) {
			// This reference was a book previously, but its type has changed, so need to delete book-specific data.

			refDAO.remove(this.bookList.get(0));
			anyChanges = true;
			
		} else if (willBeBook) {
			// This reference was not a book previously, but now will be, so we need to add book-specific data.
			
			ReferenceBook book = new ReferenceBook();
			book.set_refs_key(this._refs_key);
			book.setBook_author(rd.book_author);
			book.setBook_title(rd.book_title);
			book.setPlace(rd.place);
			book.setPublisher(rd.publisher);
			book.setCreation_date(new Date());
			book.setModification_date(book.getCreation_date()); 
			
			refDAO.persist(book);
			this.bookList.add(book);
			anyChanges = true;
		}
		return anyChanges;
	}
	
	private boolean applyNoteChanges(ReferenceDomain rd, ReferenceDAO refDAO, User currentUser) {
		// at most one note per reference
		// need to handle:  new note, updated note, deleted note
		
		boolean anyChanges = false;
		boolean hadNote = this.notes.size() > 0;
		boolean willHaveNote = (rd.referencenote != null) && (rd.referencenote.length() > 0);
		
		if (hadNote && willHaveNote) {
			// already have a note and will continue to have a note; just need to apply any difference
			
			ReferenceNote note = this.notes.get(0);
			if (!smartEqual(note.getNote(), rd.referencenote)) {
				note.setNote(rd.referencenote);
				anyChanges = true;
			}
			
		} else if (hadNote) {
			// had a note previously, but it needs to be deleted, because reference now has no note
			
			refDAO.remove(this.notes.get(0));
			anyChanges = true;
			
		} else if (willHaveNote) {
			// did not have a note previously, but now need to create one
			
			ReferenceNote note = new ReferenceNote();
			note.set_refs_key(this._refs_key);
			note.setNote(rd.referencenote);
			note.setSequenceNum(1);
			note.setCreation_date(new Date());
			note.setModification_date(note.getCreation_date()); 
			
			refDAO.persist(note);
			this.notes.add(note);
			anyChanges = true;
		}
		return anyChanges;
	}
	
	/* Apply a single ID change to this reference.  If there already is an ID for this logical database, replace it.  If there wasn't
	 * one, add one.  And, if there was one previously, but there's not now, then delete it.
	 */
	private boolean applyOneIDChange(Integer ldb, String accID, String prefixPart, Long numericPart, Integer preferred, Integer isPrivate, ReferenceDAO refDAO, User currentUser) {
		// first parameter is required; bail out if it is null
		if (ldb == null) { return false; }
		
		// First, need to find any existing AccessionID object for this logical database.

		int idPos = -1;			// position of correct ID in list of IDs
		for (int i = 0; i < accessionIDs.size(); i++) {
			AccessionID myID = accessionIDs.get(i);
			if (ldb.equals(myID.get_logicaldb_key())) {
				idPos = i;
				break;
			}
		}
		
		// If we had a previous ID for this logical database, we either need to modify it or delete it.
		if (idPos >= 0) {
			// Passing in a null ID indicates that any existing ID should be removed.
			if (accID == null) {
				refDAO.remove(accessionIDs.get(idPos));
			} else {
				// Otherwise, we can update the ID and other data for this logical database.
				AccessionID myID = accessionIDs.get(idPos);
				myID.setAccID(accID);
				myID.setIs_private(isPrivate);
				myID.setPreferred(preferred);
				myID.setPrefixPart(prefixPart);
				myID.setNumericPart(numericPart);
				myID.setModification_date(new Date());
				myID.setModifiedByUser(currentUser);
			}
		} else {
			// We didn't find an existing ID for this logical database, so we need to add one.
			
			Date creation = new Date();
			
			AccessionID myID = new AccessionID(
					refDAO.getNextAccessionKey(),
					accID,
					preferred,
					isPrivate,
					ldb,
					_refs_key,
					Constants.TYPE_REFERENCE,
					prefixPart,
					numericPart,
					creation,
					creation,
					currentUser,
					currentUser
			);
			
			refDAO.persist(myID);
		}
		return true;
	}
	
	private boolean applyAccessionIDChanges(ReferenceDomain rd, ReferenceDAO refDAO, User currentUser) {
		// consider IDs for three logical databases:  PubMed, DOI, GO REF
		// assumes only one ID per reference for each logical database (valid assumption, August 2017)
		// need to handle:  new ID for logical db, updated ID for logical db, deleted ID for logical db

		boolean anyChanges = false;
		Pattern pattern = Pattern.compile("(.*?)([0-9]+)");		// any characters as a prefix (reluctant group), followed by one or more digits
		
		if (!smartEqual(getDoiid(), rd.doiid)) {
			String prefixPart = rd.doiid;					// defaults
			Long numericPart = null;

			if (rd.doiid != null) {
				Matcher m = pattern.matcher(rd.doiid);
				if (m.find()) {
					prefixPart = m.group(1);					// ID fit pattern, so use more accurate prefix / numeric parts
					numericPart = Long.parseLong(m.group(2));
				}
			}
			
			anyChanges = applyOneIDChange(Constants.LDB_DOI, rd.doiid, prefixPart, numericPart, Constants.PREFERRED, Constants.PUBLIC, refDAO, currentUser) || anyChanges;
		}
		
		if (!smartEqual(getPubmedid(), rd.pubmedid)) {
			String prefixPart = rd.pubmedid;				// defaults
			Long numericPart = null;

			if (rd.pubmedid != null) {
				Matcher m = pattern.matcher(rd.pubmedid);
				if (m.find()) {
					prefixPart = m.group(1);					// ID fit pattern, so use more accurate prefix / numeric parts
					numericPart = Long.parseLong(m.group(2));
				}
			}
			
			anyChanges = applyOneIDChange(Constants.LDB_PUBMED, rd.pubmedid, prefixPart, numericPart, Constants.PREFERRED, Constants.PUBLIC, refDAO, currentUser) || anyChanges;
		}
		
		if (!smartEqual(getGorefid(), rd.gorefid)) {
			String prefixPart = rd.gorefid;					// defaults
			Long numericPart = null;

			if (rd.gorefid != null) {
				Matcher m = pattern.matcher(rd.gorefid);
				if (m.find()) {
					prefixPart = m.group(1);					// ID fit pattern, so use more accurate prefix / numeric parts
					numericPart = Long.parseLong(m.group(2));
				}
			}
			
			anyChanges = applyOneIDChange(Constants.LDB_GOREF, rd.gorefid, prefixPart, numericPart, Constants.SECONDARY, Constants.PRIVATE, refDAO, currentUser) || anyChanges;
		}
		return anyChanges;
	}
	
	/* handle the basic fields that have changed between this Reference and the given ReferenceDomain
	 */
	private boolean applyBasicFieldChanges(ReferenceDomain rd, ReferenceDAO refDAO, User currentUser) {
		// exactly one set of basic data per reference, including:  is_discard flag, reference type,
		// author, primary author (derived), journal, title, volume, issue, date, year, pages, 
		// abstract, and isReviewArticle flag

		boolean anyChanges = false;
		
		// determine if the is_discard flag is set in the ReferenceDomain object
		int rdDiscard = 0;
		if ("1".equals(rd.is_discard) || ("Yes".equalsIgnoreCase(rd.is_discard))) {
			rdDiscard = 1;
		}
		
		// determine if the isReviewArticle flag is set in the ReferenceDomain object
		int rdReview = 0;
		if ("1".equals(rd.isReviewArticle) || ("Yes".equalsIgnoreCase(rd.isReviewArticle))) {
			rdReview = 1;
		}
		
		String refType = this.referenceTypeTerm.getTerm();
		
		// update this object's data to match what was passed in
		if ((rdDiscard != this.is_discard) || (rdReview != this.isReviewArticle)
				|| !smartEqual(this.authors, rd.authors)
				|| !smartEqual(this.journal, rd.journal)
				|| !smartEqual(this.title, rd.title)
				|| !smartEqual(this.volume, rd.volume)
				|| !smartEqual(this.issue, rd.issue)
				|| !smartEqual(this.date, rd.date)
				|| !smartEqual(this.year, rd.year)
				|| !smartEqual(refType, rd.reference_type)
				|| !smartEqual(this.pages, rd.pages)
				|| !smartEqual(this.ref_abstract, rd.ref_abstract)
			) {
			
			if (rd.authors != null) {
				Pattern pattern = Pattern.compile("([^;]+).*");		// any characters up to the first semicolon are the primary author
				Matcher matcher = pattern.matcher(rd.authors);
				if (matcher.find()) {
					this.primary_author = matcher.group(1);
				}
			}

			this.is_discard = rdDiscard;
			this.isReviewArticle = rdReview;
			this.authors = rd.authors;
			this.journal = rd.journal;
			this.title = rd.title;
			this.volume = rd.volume;
			this.issue = rd.issue;
			this.date = rd.date;
			this.year = rd.year;
			this.pages = rd.pages;
			this.ref_abstract = rd.ref_abstract;
			this.referenceTypeTerm = refDAO.getTermByTerm(Constants.VOC_REFERENCE_TYPE, rd.reference_type);
			this.setModificationInfo(currentUser);
			anyChanges = true;
		}

		return anyChanges;
	}
	
	/* take the data from the domain object and overwrite any changed data into this object
	 * (assumes we are working in a transaction and persists any sub-objects into the database, but does
	 * not persist this Reference object itself, as other changes could be coming)
	 */
	@Transient
	public void applyDomainChanges(ReferenceDomain rd, ReferenceDAO refDAO, User currentUser) throws Exception {
		// note that we must have 'anyChanges' after the OR, otherwise short-circuit evaluation will only save
		// the first section changed
		
		boolean anyChanges = applyStatusChanges(rd, refDAO, currentUser);
		anyChanges = applyTagChanges(rd, refDAO, currentUser) || anyChanges;
		anyChanges = applyBasicFieldChanges(rd, refDAO, currentUser) || anyChanges;
		anyChanges = applyBookChanges(rd, refDAO, currentUser) || anyChanges;
		anyChanges = applyNoteChanges(rd, refDAO, currentUser) | anyChanges;
		anyChanges = applyAccessionIDChanges(rd, refDAO, currentUser) || anyChanges;
		anyChanges = applyWorkflowDataChanges(rd, refDAO, currentUser) || anyChanges;
		if (anyChanges) {
			this.setModificationInfo(currentUser);
		}
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

	/* If this reference has workflow data, return an object with the extra workflow data;
	 * otherwise return null.
	 */
	@Transient
	public ReferenceWorkflowData getWorkflowData() {
		if (this.workflowData.size() > 0) {
			return this.workflowData.get(0);
		}
		return null;
	}
}
