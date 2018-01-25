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
import org.jax.mgi.mgd.api.model.EntityBase;
import org.jax.mgi.mgd.api.model.acc.entities.Accession;
import org.jax.mgi.mgd.api.model.acc.entities.LogicalDB;
import org.jax.mgi.mgd.api.model.gxd.entities.Antibody;
import org.jax.mgi.mgd.api.model.map.entities.CoordFeature;
import org.jax.mgi.mgd.api.model.mgi.entities.MGISynonym;
import org.jax.mgi.mgd.api.model.mgi.entities.Organism;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeMarker;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Marker Model Object")
@Table(name="mrk_marker")
public class Marker extends EntityBase {

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

	//@JsonIgnore
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_organism_key", referencedColumnName="_organism_key")
	private Organism organism;

	//@ApiModelProperty(value="Controlled vocabulary table for all Marker Statuses (approved, withdrawn)")
	//@JsonIgnore
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_marker_status_key", referencedColumnName="_marker_status_key")
	private Status markerStatus;

	//need MRK_Types entity
	//@ApiModelProperty(value="Controlled vocabulary table for all Marker Types")
	//@JsonIgnore
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_marker_type_key", referencedColumnName="_marker_type_key")
	private Type markerType;

	@JsonIgnore
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@JsonIgnore
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;

	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_marker_key", referencedColumnName="_object_key")
	@Where(clause="_mgitype_key = 2 AND preferred = 1 and _logicaldb_key = 1")
	private Accession mgiAccessionId;

	@OneToMany(fetch=FetchType.EAGER)
	@JoinColumn(name="_object_key", referencedColumnName="_marker_key")
	@Where(clause="_mgitype_key = 2 AND preferred = 1")
	private Set<Accession> allAccessionIds;
	
	@OneToMany(fetch=FetchType.EAGER)
	@JoinColumn(name="_marker_key", referencedColumnName="_marker_key")
	private Set<Offset> offsets;
	
	@OneToMany(fetch=FetchType.EAGER)
	@JoinColumn(name="_marker_key", referencedColumnName="_marker_key")
	private Set<ProbeMarker> probeMarkers;
	
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name = "mrk_alias",
		joinColumns = @JoinColumn(name = "_alias_key", referencedColumnName="_marker_key"),
		inverseJoinColumns = @JoinColumn(name = "_marker_key", referencedColumnName="_marker_key")
	)
	private Set<Marker> aliases;

	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name = "mrk_current",
		joinColumns = @JoinColumn(name = "_current_key", referencedColumnName="_marker_key"),
		inverseJoinColumns = @JoinColumn(name = "_marker_key", referencedColumnName="_marker_key")
	)
	private Set<Marker> currentMarkers;

	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name = "gxd_antibody_marker",
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
	
	@OneToMany(fetch=FetchType.EAGER)	@JoinColumn(name="_object_key", referencedColumnName="_marker_key")
	@Where(clause="_mgitype_key = 2")
	private Set<CoordFeature> features;

	@OneToMany(fetch=FetchType.EAGER)	@JoinColumn(name="_object_key", referencedColumnName="_marker_key")
	@Where(clause="_mgitype_key = 2")
	private Set<MGISynonym> synonyms;
}
