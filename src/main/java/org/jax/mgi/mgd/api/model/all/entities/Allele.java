package org.jax.mgi.mgd.api.model.all.entities;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Where;
import org.jax.mgi.mgd.api.model.EntityBase;
import org.jax.mgi.mgd.api.model.acc.entities.Accession;
import org.jax.mgi.mgd.api.model.acc.entities.LogicalDB;
import org.jax.mgi.mgd.api.model.bib.entities.Reference;
import org.jax.mgi.mgd.api.model.mgi.entities.MGISynonym;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mrk.entities.Marker;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeStrain;
import org.jax.mgi.mgd.api.model.voc.entities.Term;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Allele Model Object")
@Table(name="all_allele")
public class Allele extends EntityBase {

	@Id
	private Integer _allele_key;
	private String symbol;
	private String name;
	private Integer isWildType;
	private Integer isExtinct;
	private Integer isMixed;
	private Date approval_date;
	private Date creation_date;
	private Date modification_date;
	
	//@JsonIgnore
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_marker_key")
	private Marker marker;
	
	//@JsonIgnore
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_strain_key")
	private ProbeStrain strain;
	
	//@JsonIgnore
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_mode_key", referencedColumnName="_term_key")
	private Term inheritanceMode;
	
	//@JsonIgnore
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_allele_type_key", referencedColumnName="_term_key")
	private Term alleleType;
	
	//@JsonIgnore
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_allele_status_key", referencedColumnName="_term_key")
	private Term alleleStatus;
	
	//@JsonIgnore
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_transmission_key", referencedColumnName="_term_key")
	private Term transmission;
	
	//@JsonIgnore
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_collection_key", referencedColumnName="_term_key")
	private Term collection;
	
	//@JsonIgnore
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_refs_key")
	private Reference reference;
	
	//@JsonIgnore
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_markerallele_status_key", referencedColumnName="_term_key")
	private Term markerAlleleStatus;
	
	//@JsonIgnore
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	//@JsonIgnore
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;
	
	//@JsonIgnore
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_approvedby_key", referencedColumnName="_user_key")
	private User approvedBy;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_allele_key", referencedColumnName="_object_key")
	@Where(clause="`_mgitype_key` = 11 AND preferred = 1 AND `_logicaldb_key` = 1")
	private Accession mgiAccessionId;

	@OneToMany(fetch=FetchType.LAZY)	@JoinColumn(name="_object_key", referencedColumnName="_allele_key")
	@Where(clause="`_mgitype_key` = 11")
	private Set<MGISynonym> synonyms;
	
	@OneToMany(fetch=FetchType.LAZY)
	@JoinColumn(name="_object_key", referencedColumnName="_allele_key")
	@Where(clause="`_mgitype_key` = 11 AND preferred = 1")
	private Set<Accession> allAccessionIds;
	
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name = "all_allele_mutation",
		joinColumns = @JoinColumn(name = "_allele_key"),
		inverseJoinColumns = @JoinColumn(name = "_mutation_key", referencedColumnName="_term_key")
	)
	private Set<Term> mutations;
	
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name = "prb_allele_strain",
		joinColumns = @JoinColumn(name = "_allele_key"),
		inverseJoinColumns = @JoinColumn(name = "_strain_key")
	)
	private Set<ProbeStrain> strains;
	

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
