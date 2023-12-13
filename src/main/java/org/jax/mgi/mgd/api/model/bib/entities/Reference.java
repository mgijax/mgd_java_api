package org.jax.mgi.mgd.api.model.bib.entities;

import java.util.Date;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.hibernate.annotations.Where;
import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.acc.entities.Accession;
import org.jax.mgi.mgd.api.model.mgi.entities.MGIReferenceAssoc;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.entities.Term;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderBy;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Schema(description = "Reference Model Object")
@Table(name="bib_refs")
public class Reference extends BaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="bib_refs_generator")
	@SequenceGenerator(name="bib_refs_generator", sequenceName = "bib_refs_seq", allocationSize=1)
	@Schema(name="primary key")	
	private int _refs_key;
	private String authors;
	private String title;
	private String journal;
	private String vol;
	private String issue;
	private String date;
	private int year;
	private String pgs;
	@Column(columnDefinition = "int2")
	private int isReviewArticle;
	private Date creation_date;
	private Date modification_date;
	
	@Column(name="_primary")
	private String primaryAuthor;	
	@Column(name="abstract")
	private String referenceAbstract;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_referencetype_key", referencedColumnName="_term_key")
	private Term referenceTypeTerm;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_refs_key")
	private ReferenceCitationCache referenceCitationCache;

//	// at most one mapping note
//	@OneToMany()
//	@JoinColumn(name="_refs_key", insertable=false, updatable=false)
//	private List<MappingNote> mappingNote;
	
	// mgi accession ids only
	@OneToMany()	
	@JoinColumn(name="_object_key", referencedColumnName="_refs_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 1 and `_logicaldb_key` = 1")
	@OrderBy("prefixPart desc, preferred desc, accID")
	private List<Accession> mgiAccessionIds;
	
	// editable only accession ids
	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_refs_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 1 and `_logicaldb_key` in (1, 29, 65, 185)")
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
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_refs_key")
	private ReferenceAssociatedData associatedData;

	// reference allele associations : alleles (11)
	@OneToMany()
	@JoinColumn(name="_refs_key", referencedColumnName="_refs_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` in (11)")
	@OrderBy("_refassoctype_key")
	private List<MGIReferenceAssoc> alleleAssocs;
	
}
