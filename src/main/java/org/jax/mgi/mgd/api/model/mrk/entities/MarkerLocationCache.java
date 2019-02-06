package org.jax.mgi.mgd.api.model.mrk.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.mgi.entities.Organism;
import org.jax.mgi.mgd.api.model.mgi.entities.User;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Location Cache Model Object")
@Table(name="mrk_location_cache")
public class MarkerLocationCache extends BaseEntity {

	@Id
	private Integer _marker_key;
	private String chromosome;
	private Integer sequenceNum;
	private String cytogeneticOffset;
	private Integer cmOffset;
	private String genomicChromosome;
	private Integer startCoordinate;
	private Integer endCoordinate;
	private String strand;
	private String mapUnits;
	private String provider;
	private String version;
	private Date creation_date;
	private Date modification_date;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_marker_type_key")
	private MarkerType markerType;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_organism_key")
	private Organism organism;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;
	
}
