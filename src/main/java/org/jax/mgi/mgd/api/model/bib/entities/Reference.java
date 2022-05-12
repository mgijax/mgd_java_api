package org.jax.mgi.mgd.api.model.bib.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Where;
import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.acc.entities.Accession;
import org.jax.mgi.mgd.api.model.mgi.entities.MGIReferenceAssoc;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mld.entities.MappingNote;
import org.jax.mgi.mgd.api.model.voc.entities.Term;
import org.jax.mgi.mgd.api.util.Constants;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Reference Model Object")
@Table(name="bib_refs")
public class Reference extends BaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="bib_refs_generator")
	@SequenceGenerator(name="bib_refs_generator", sequenceName = "bib_refs_seq", allocationSize=1)
	@ApiModelProperty(value="primary key")	
	private int _refs_key;
	private String authors;
	private String title;
	private String journal;
	private String vol;
	private String issue;
	private String date;
	private int year;
	private String pgs;
	private int isReviewArticle;
	private Date creation_date;
	private Date modification_date;
	
	@Column(name="_primary")
	private String primaryAuthor;	
	@Column(name="abstract")
	private String referenceAbstract;

	@OneToOne()
	@JoinColumn(name="_referencetype_key", referencedColumnName="_term_key")
	private Term referenceTypeTerm;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_refs_key", insertable=false, updatable=false)
	private ReferenceCitationCache referenceCitationCache;

	// at most one mapping note
	@OneToMany()
	@JoinColumn(name="_refs_key", insertable=false, updatable=false)
	private List<MappingNote> mappingNote;
	
	// mgi accession ids only
	@OneToMany()	
	@JoinColumn(name="_object_key", referencedColumnName="_refs_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 1 and `_logicaldb_key` = 1")
	@OrderBy("prefixPart desc, preferred desc, accID")
	private List<Accession> mgiAccessionIds;
	
	// editable only accession ids
	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_refs_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 1 and `_logicaldb_key` in (29, 65, 185)")
	@OrderBy("accid")
	private List<Accession> editAccessionIds;

	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_refs_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 1")
	@OrderBy("_logicaldb_key, preferred desc")
	private List<Accession> accessionIDs;

	@OneToMany()
	@JoinColumn(name="_refs_key", insertable=false, updatable=false)
	@OrderBy("modification_date desc, isCurrent desc, _group_key")	
	private List<ReferenceWorkflowStatus> workflowStatus;
	
	@OneToMany()
	@JoinColumn(name="_refs_key", insertable=false, updatable=false)
	@Where(clause="`iscurrent` = 1")	
	private List<ReferenceWorkflowStatus> workflowStatusCurrent;
	
	// workflow relevance
	@OneToMany()
	@JoinColumn(name="_refs_key", insertable=false, updatable=false)
	@OrderBy("modification_date desc, isCurrent desc")
	private List<ReferenceWorkflowRelevance> workflowRelevance;	
	
	@OneToMany()
	@JoinColumn(name="_refs_key", insertable=false, updatable=false)
	private List<ReferenceWorkflowTag> workflowTags;
	
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
	
	// one to one, because counts will always exist
	@OneToOne()
	@JoinColumn(name="_refs_key")
	private ReferenceAssociatedData associatedData;

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
		return findFirstID(1, "J:", 1, 0);
	}

	@Transient
	public String getDoiid() {
		return findFirstID(65, null, null, null);
	}

	@Transient
	public String getPubmedid() {
		return findFirstID(29, null, null, null);
	}

	@Transient
	public String getMgiid() {
		return findFirstID(1, "MGI:", null, null);
	}

	@Transient
	public String getGorefid() {
		return findFirstID(185, null, null, null);
	}	
}
