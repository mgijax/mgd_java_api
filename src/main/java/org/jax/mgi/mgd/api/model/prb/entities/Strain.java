package org.jax.mgi.mgd.api.model.prb.entities;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
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
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.entities.Term;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Strain Model Object")
@Table(name="prb_strain")
public class Strain extends EntityBase {

	@Id
	private Integer _strain_key;
	private String strain;
	private Integer standard;

	@Column(name="private") // just "private" is a Java reserved word
	private Integer is_private;

	private Integer geneticBackground;
	private Date creation_date;
	private Date modification_date;
	
	@JsonIgnore
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_species_key", referencedColumnName="_term_key")
	private Term species;

	@JsonIgnore
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_straintype_key", referencedColumnName="_term_key")
	private Term strainType;

	@JsonIgnore
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@JsonIgnore
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;
	
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name = "prb_allele_strain",
		joinColumns = @JoinColumn(name = "_strain_key"),
		inverseJoinColumns = @JoinColumn(name = "_allele _key")
	)
	private Set<ProbeAllele> alleles;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_strain_key", referencedColumnName="_object_key")
	@Where(clause="_mgitype_key = 10 AND preferred = 1 AND _logicaldb_key = 1")
	private Accession mgiAccessionId;

	@OneToMany(fetch=FetchType.EAGER)
	@JoinColumn(name="_object_key", referencedColumnName="_strain_key")
	@Where(clause="_mgitype_key = 10 AND preferred = 1")
	private Set<Accession> allAccessionIds;
	
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
