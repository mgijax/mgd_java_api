package org.jax.mgi.mgd.api.model.voc.entities;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Where;
import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.acc.entities.Accession;
import org.jax.mgi.mgd.api.model.acc.entities.LogicalDB;
import org.jax.mgi.mgd.api.model.mgi.entities.MGISynonym;
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
	private Integer isObsolete = 0; // Default for creating new terms (Don't change)
	private Date creation_date;
	private Date modification_date;

	@OneToOne
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;

	@OneToOne
	@JoinColumn(name="_term_key", referencedColumnName="_object_key")
	@Where(clause="`_mgitype_key` = 13 AND preferred = 1 AND `_logicaldb_key` = 1")
	private Accession mgiTermAccessionId;

	@OneToMany
	@JoinColumn(name="_object_key", referencedColumnName="_term_key")
	@Where(clause="`_mgitype_key` = 13 AND preferred = 1")
	private Set<Accession> allAccessionIds;

	@OneToMany
	@JoinColumn(name="_object_key", referencedColumnName="_term_key")
	@Where(clause="`_mgitype_key` = 13")
	private Set<MGISynonym> synonyms;

	@ManyToOne
	@JoinColumn(name="_vocab_key")
	private Vocabulary vocab;
	
	@Transient
	public Set<Accession> getAccessionIdsByLogicalDb(LogicalDB db) {
		return getAccessionIdsByLogicalDb(db.get_logicaldb_key());
	}
	
	@Transient
	public Set<Accession> getAccessionIdsByLogicalDb(Integer db_key) {
		HashSet<Accession> set = new HashSet<Accession>();
		for(Accession a: allAccessionIds) {
			if(a.get_logicaldb_key() == db_key) {
				set.add(a);
			}
		}
		return set;
	}

}
