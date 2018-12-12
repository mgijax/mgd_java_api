package org.jax.mgi.mgd.api.model.voc.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.mgi.entities.User;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name="voc_term")
public class Term extends BaseEntity {

	@Id
	private Integer _term_key;
	private String term;
	private String abbreviation;
	private String note;
	private Integer sequenceNum;
	private Integer isObsolete = 0;
	private Date creation_date;
	private Date modification_date;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_vocab_key")
	private Vocabulary vocab;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;
	
	//@OneToOne(fetch=FetchType.LAZY)
	//@JoinColumn(name="_term_key", referencedColumnName="_object_key")
	//@Where(clause="`_mgitype_key` = 13 AND preferred = 1 AND `_logicaldb_key` = 1")
	//private Accession mgiTermAccessionId;
	
	//@OneToMany
	//@JoinColumn(name="_object_key", referencedColumnName="_term_key")
	//@Where(clause="`_mgitype_key` = 13")
	//private List<MGISynonym> synonyms;

	//@Transient
	//public List<Accession> getAccessionIdsByLogicalDb(LogicalDB db) {
	//	return getAccessionIdsByLogicalDb(db.get_logicaldb_key());
	//}
	
	//@Transient
	//public List<Accession> getAccessionIdsByLogicalDb(Integer db_key) {
	//	HashSet<Accession> set = new HashSet<Accession>();
	//	for(Accession a: allAccessionIds) {
	//		if(a.getLogicaldb().get_logicaldb_key() == db_key) {
	//			set.add(a);
	//		}
	//	}
	//	return set;
	//}

}
