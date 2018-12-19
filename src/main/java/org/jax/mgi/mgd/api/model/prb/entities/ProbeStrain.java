package org.jax.mgi.mgd.api.model.prb.entities;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Where;
import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.acc.entities.Accession;
import org.jax.mgi.mgd.api.model.mgi.entities.MGISynonym	;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.entities.Term;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Strain Model Object")
@Table(name="prb_strain")
public class ProbeStrain extends BaseEntity {

	@Id
	private int _strain_key;
	private String strain;
	private int standard;
	@Column(name="private") // just "private" is a Java reserved word
	private int is_private;
	private int geneticBackground;
	private Date creation_date;
	private Date modification_date;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_species_key", referencedColumnName="_term_key")
	private Term species;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_straintype_key", referencedColumnName="_term_key")
	private Term strainType;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_strain_key", referencedColumnName="_object_key")
	@Where(clause="`_mgitype_key` = 10 AND preferred = 1 AND `_logicaldb_key` = 1")
	private Accession mgiAccessionId;

	@OneToMany
	@JoinColumn(name="_strain_key")
	private Set<ProbeStrainMarker> probeStrainMarkers;
	
	@OneToMany
	@JoinColumn(name="_strain_key")
	private Set<ProbeStrainGenotype> probeStrainGenotypes;
	
	@OneToMany
	@JoinColumn(name="_object_key", referencedColumnName="_strain_key")
	@Where(clause="`_mgitype_key` = 10 AND preferred = 1")
	private Set<Accession> allAccessionIds;
	
	@OneToMany
	@JoinColumn(name="_object_key", referencedColumnName="_strain_key")
	@Where(clause="`_mgitype_key` = 10")
	private Set<MGISynonym> synonyms;
	
	//@ManyToMany
	//@JoinTable(name = "prb_allele_strain",
	//	joinColumns = @JoinColumn(name = "_strain_key"),
	//	inverseJoinColumns = @JoinColumn(name = "_allele_key")
	//)
	//private Set<ProbeAllele> alleles;

	//@Transient
	//public Set<Accession> getAccessionIdsByLogicalDb(LogicalDB db) {
	//	return getAccessionIdsByLogicalDb(db.get_logicaldb_key());
	//}
	
	//@Transient
	//public Set<Accession> getAccessionIdsByLogicalDb(Integer db_key) {
	//	HashSet<Accession> set = new HashSet<Accession>();
	//	for(Accession a: allAccessionIds) {
	//		if(a.getLogicaldb().get_logicaldb_key() == db_key) {
	//			set.add(a);
	//		}
	//	}
	//	return set;
	//}
	
}
