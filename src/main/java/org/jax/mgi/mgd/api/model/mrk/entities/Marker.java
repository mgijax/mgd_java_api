package org.jax.mgi.mgd.api.model.mrk.entities;

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
import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.acc.entities.Accession;
import org.jax.mgi.mgd.api.model.acc.entities.LogicalDB;
import org.jax.mgi.mgd.api.model.all.entities.Allele;
import org.jax.mgi.mgd.api.model.gxd.entities.Antibody;
import org.jax.mgi.mgd.api.model.gxd.entities.Assay;
import org.jax.mgi.mgd.api.model.gxd.entities.Index;
import org.jax.mgi.mgd.api.model.map.entities.CoordinateFeature;
import org.jax.mgi.mgd.api.model.mgi.entities.MGISynonym;
import org.jax.mgi.mgd.api.model.mgi.entities.Organism;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mld.entities.ExptMarker;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeMarker;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeStrainMarker;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name="mrk_marker")
public class Marker extends BaseEntity {

	@Id
	@ApiModelProperty(value="primary key")
	private Integer _marker_key;

	@ApiModelProperty(value="official symbol of marker")
	private String symbol;

	@ApiModelProperty(value="official name of marker")
	private String name;

	@ApiModelProperty(value="chromosome")
	private String chromosome;

	@ApiModelProperty(value="cytogenetic band")
	private String cytogeneticOffset;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_organism_key")
	private Organism organism;

	//@ApiModelProperty(value="Controlled vocabulary table for all Marker Statuses (official, withdrawn, reserved)")
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_marker_status_key")
	private MarkerStatus markerStatus;

	//@ApiModelProperty(value="Controlled vocabulary table for all Marker Types")
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_marker_type_key")
	private MarkerType markerType;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;

	public Accession getMgiAccessionId() {
		for(Accession a: allAccessionIds) {
			if(a.get_mgitype_key() == 2 && a.getPreferred() == 1 && a.get_logicaldb_key() == 1) {
				return a;
			}
		}
		return null;
	}

	@OneToMany
	@JoinColumn(name="_object_key", referencedColumnName="_marker_key")
	@Where(clause="`_mgitype_key` = 2 AND preferred = 1")
	private Set<Accession> allAccessionIds;
	
	@OneToMany
	@JoinColumn(name="_marker_key")
	private Set<MarkerHistory> history;
	
	@OneToMany
	@JoinColumn(name="_marker_key")
	private Set<Allele> alleles = new HashSet<Allele>();
	
	@OneToMany
	@JoinColumn(name="_marker_key")
	private Set<MarkerOffset> offsets;
	
	@OneToMany
	@JoinColumn(name="_marker_key")
	private Set<MarkerStrain> markerStrain;
	
	@OneToMany
	@JoinColumn(name="_marker_key")
	private Set<ProbeMarker> probeMarkers;

	@OneToMany
	@JoinColumn(name="_marker_key")
	private Set<ProbeStrainMarker> probeStrainMarkers;
	
	@OneToMany
	@JoinColumn(name="_marker_key")
	private Set<Assay> assays;
	
	@OneToMany
	@JoinColumn(name="_marker_key")
	private Set<Index> indexes;
	
	@OneToMany
	@JoinColumn(name="_marker_key")
	private Set<ExptMarker> exptMarkers;
	
	@OneToMany
	@JoinColumn(name="_object_key", referencedColumnName="_marker_key")
	@Where(clause="`_mgitype_key` = 2")
	private Set<CoordinateFeature> features;

	@OneToMany
	@JoinColumn(name="_object_key", referencedColumnName="_marker_key")
	@Where(clause="`_mgitype_key` = 2")
	private Set<MGISynonym> synonyms;

	@ManyToMany
	@JoinTable(name = "mrk_alias",
		joinColumns = @JoinColumn(name = "_alias_key", referencedColumnName="_marker_key"),
		inverseJoinColumns = @JoinColumn(name = "_marker_key", referencedColumnName="_marker_key")
	)
	private Set<Marker> aliases;

	@ManyToMany
	@JoinTable(name = "mrk_current",
		joinColumns = @JoinColumn(name = "_current_key", referencedColumnName="_marker_key"),
		inverseJoinColumns = @JoinColumn(name = "_marker_key", referencedColumnName="_marker_key")
	)
	private Set<Marker> currentMarkers;

	@ManyToMany
	@JoinTable(name = "gxd_antibodymarker",
		joinColumns = @JoinColumn(name = "_marker_key"),
		inverseJoinColumns = @JoinColumn(name = "_antibody_key")
	)
	private Set<Antibody> antibodies;

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
