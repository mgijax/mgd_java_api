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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.OrderBy;
import org.hibernate.annotations.Where;
import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.acc.entities.Accession;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mld.entities.MappingNote;
import org.jax.mgi.mgd.api.model.voc.entities.Term;

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
	//private int isDiscard;
	private Date creation_date;
	private Date modification_date;

	@Column(name="abstract")
	private String referenceAbstract;

	@Column(name="_primary")
	private String primaryAuthor;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_referencetype_key", referencedColumnName="_term_key")
	private Term referenceType;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_refs_key", insertable=false, updatable=false)
	private ReferenceCitationCache referenceCitationCache;

	// at most one reference book
	@OneToMany()
	@JoinColumn(name="_refs_key", insertable=false, updatable=false)
	private List<ReferenceBook> referenceBook;
	
	// at most one book note
	@OneToMany()
	@JoinColumn(name="_refs_key", insertable=false, updatable=false)
	private List<ReferenceNote> referenceNote;

//	// at most one mapping note
//	@OneToMany()
//	@JoinColumn(name="_refs_key", insertable=false, updatable=false)
//	private List<MappingNote> mappingNote;
	
	// mgi accession ids only
	@OneToMany()	
	@JoinColumn(name="_object_key", referencedColumnName="_refs_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 1 and `_logicaldb_key` = 1")
	@OrderBy(clause="prefixPart desc, preferred desc, accID")
	private List<Accession> mgiAccessionIds;
	
	// editable only accession ids
	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_refs_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 1 and `_logicaldb_key` in (29, 65, 185)")
	@OrderBy(clause ="accid")
	private List<Accession> editAccessionIds;
	
}
