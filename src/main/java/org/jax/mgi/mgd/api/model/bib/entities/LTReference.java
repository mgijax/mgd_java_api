package org.jax.mgi.mgd.api.model.bib.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Where;
import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.acc.entities.Accession;
import org.jax.mgi.mgd.api.model.mgi.entities.MGIReferenceAssoc;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.entities.Term;
import org.jax.mgi.mgd.api.util.Constants;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Reference Model Object")
@Table(name="bib_refs")
public class LTReference extends BaseEntity {

	@Id
	private Integer _refs_key;
	private String authors;
	private String title;
	private String journal;
	private String vol;
	private String issue;
	private String date;
	private Integer year;
	private String pgs;
	private int isReviewArticle;
	private Date creation_date;
	private Date modification_date;

	@Column(name="_primary")
	private String primaryAuthor;	
	@Column(name="abstract")
	private String referenceAbstract;		// just "abstract" is a Java reserved word, so need a prefix
	
	@OneToOne()
	@JoinColumn(name="_refs_key", referencedColumnName="_refs_key", insertable=false, updatable=false)
	private LTReferenceStatusView statusView;

	@OneToOne()
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne()
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;

	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_refs_key")
	@Where(clause="`_mgitype_key` = 1")
	@OrderBy("_logicaldb_key, preferred desc")
	private List<Accession> accessionIDs;

	@OneToOne()
	@JoinColumn(name="_referencetype_key", referencedColumnName="_term_key")
	private Term referenceTypeTerm;

	@OneToMany()
	@JoinColumn(name="_refs_key", insertable=false, updatable=false)
	@OrderBy("isCurrent desc, _group_key, modification_date")	
	private List<ReferenceWorkflowStatus> workflowStatus;
	
	// workflow relevance
	@OneToMany()
	@JoinColumn(name="_refs_key", insertable=false, updatable=false)
	@OrderBy("isCurrent desc")
	private List<ReferenceWorkflowRelevance> workflowRelevance;	
	
	@OneToMany()
	@JoinColumn(name="_refs_key", insertable=false, updatable=false)
	private List<LTReferenceWorkflowTag> workflowTags;
	
	// only interested in workflow data where extracted text section = 'body' (48804490)
	@OneToMany()
	@JoinColumn(name="_refs_key", insertable=false, updatable=false)
	@Where(clause="`_extractedtext_key` = 48804490")
	private List<ReferenceWorkflowData> workflowData;
	
	// at most one note
	@OneToMany()
	@JoinColumn(name="_refs_key", insertable=false, updatable=false)
	private List<ReferenceNote> referenceNote;

	// one to many, because book data most often does not exist (leaving it 1-0)
	@OneToMany()
	@JoinColumn(name="_refs_key", insertable=false, updatable=false)
	private List<ReferenceBook> referenceBook;
	
	// one to many, because row in citation cache might not exist (leaving it 1-0)
	@OneToMany()
	@JoinColumn(name="_refs_key")
	private List<ReferenceCitationCache> citationData;
	
	// one to one, because counts will always exist
	@OneToOne()
	@JoinColumn(name="_refs_key")
	private LTReferenceAssociatedData associatedData;

	// reference allele associations : alleles (11)
	@OneToMany()
	@JoinColumn(name="_refs_key", referencedColumnName="_refs_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` in (11)")
	@OrderBy("_refassoctype_key")
	private List<MGIReferenceAssoc> alleleAssocs;
	

	/* Find and return the first accession ID matching any specified logical database, prefix,
	 * is-preferred, and is-private settings.
	 */
	@Transient
	private String findFirstID(Integer ldb, String prefix, Integer preferred, Integer isPrivate) {
		for (int i = 0; i < accessionIDs.size(); i++) {
			Accession accID = accessionIDs.get(i);
			if ((ldb == null) || (ldb.equals(accID.getLogicaldb().get_logicaldb_key()))) {
				if ((prefix == null) || prefix.equals(accID.getPrefixPart())) {
					if ((preferred == null) || (preferred.equals(accID.getPreferred()))) {
						if ((isPrivate == null) || (isPrivate.equals(accID.getIsPrivate()))) {
							return accID.getAccID();
						}
					}
				}
			}
		}
		return null;
	}

	@Transient
	public String getJnumid() {
		return findFirstID(Constants.LDB_MGI, "J:", Constants.PREFERRED, Constants.PUBLIC);
	}

	@Transient
	public String getDoiid() {
		return findFirstID(Constants.LDB_DOI, null, null, null);
	}

	@Transient
	public String getPubmedid() {
		return findFirstID(Constants.LDB_PUBMED, null, null, null);
	}

	@Transient
	public String getMgiid() {
		return findFirstID(Constants.LDB_MGI, "MGI:", null, null);
	}

	@Transient
	public String getGorefid() {
		return findFirstID(Constants.LDB_GOREF, null, null, null);
	}

	// bib_citation_cache
	
	@Transient
	public String getCachedID(String provider) {
		citationData.size(); // loads it
		if ((citationData == null) || (citationData.size() == 0)) { return null; }
		if ("MGI".equals(provider)) {
			return citationData.get(0).getMgiid();
		} else if ("J:".equals(provider)) {
			return citationData.get(0).getJnumid();
		} else if ("DOI".equals(provider)) {
			return citationData.get(0).getDoiid();
		} else {
			return citationData.get(0).getPubmedid();
		}
	}
	
	// bib_workflow_tag

	@Transient
	public List<String> getWorkflowTagsAsStrings() {
		List<String> tags = new ArrayList<String>();
		for (LTReferenceWorkflowTag rwTag : workflowTags) {
			tags.add(rwTag.getTag().getTerm());
		}
		Collections.sort(tags);
		return tags;
	}
	
	// bib_workflowStatus
	
	// maps workflow group abbrev to current status for that group, cached in memory for efficiency - not persisted
	@Transient
	private Map<String,String> workflowStatusCache;
	
	@Transient
	public void clearWorkflowStatusCache() {
		workflowStatusCache = null;
	}

	@Transient
	private void buildWorkflowStatusCache() {
		workflowStatusCache = new HashMap<String,String>();
		for (ReferenceWorkflowStatus rws : workflowStatus) {
			if (rws.getIsCurrent() == 1) {
				workflowStatusCache.put(rws.getStatusTerm().getAbbreviation(), rws.getStatusTerm().getAbbreviation());
			}
		}
	}

	@Transient
	public String getStatus(String groupAbbrev) {
		if (workflowStatusCache == null) { buildWorkflowStatusCache(); }
		if (workflowStatusCache.containsKey(groupAbbrev)) {
			return workflowStatusCache.get(groupAbbrev);
		}
		return null;
	}

}
