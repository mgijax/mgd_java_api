package org.jax.mgi.mgd.api.model.bib.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.entities.Term;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Reference Model Object")
@Table(name="bib_refs")
public class Reference extends BaseEntity {

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
	private Integer isReviewArticle;
	private Integer isDiscard;
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
	@JoinColumn(name="_refs_key")
	private ReferenceCitationCache referenceCitationCache;
	
	// mgi accession ids only
	//@OneToMany
	//@JoinColumn(name="_object_key", referencedColumnName="_refs_key", insertable=false, updatable=false)
	//@Where(clause="`_mgitype_key` = 1 and `_logicaldb_key` = 1")
	//@OrderBy(clause="preferred desc, accID")
	//private List<Accession> mgiAccessionIds;

	//@OneToOne(fetch=FetchType.LAZY)
	//@JoinColumn(name="_refs_key", referencedColumnName="_object_key")
	//@Where(clause="`_mgitype_key` = 1 AND preferred = 1 AND prefixPart = 'MGI:' AND `_logicaldb_key` = 1")
	//private Accession mgiAccessionId;
	
	//@OneToOne(fetch=FetchType.LAZY)
	//@JoinColumn(name="_refs_key", referencedColumnName="_object_key")
	//@Where(clause="`_mgitype_key` = 1 AND preferred = 1 AND prefixPart = 'J:' AND `_logicaldb_key` = 1")
	//private Accession jnumAccessionId;
	
	//@OneToMany
	//@JoinColumn(name="_object_key", referencedColumnName="_refs_key")
	//@Where(clause="`_mgitype_key` = 1 AND preferred = 1")
	//private Set<Accession> allAccessionIds;
	
	//@Transient
	//public Set<Accession> getAccessionIdsByLogicalDb(LogicalDB db) {
	//	return getAccessionIdsByLogicalDb(db.get_logicaldb_key());
	//}
	
	//@Transient
	//public Set<Accession> getAccessionIdsByLogicalDb(Integer db_key) {
	//	HashSet<Accession> set = new HashSet<Accession>();
	//	for(Accession a: allAccessionIds) {
	//		if(a.get_logicaldb_key() == db_key) {
	//			set.add(a);
	//		}
	//	}
	//	return set;
	//}
	
}
