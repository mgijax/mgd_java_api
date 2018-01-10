package org.jax.mgi.mgd.api.model.mrk.entities;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Where;
import org.jax.mgi.mgd.api.model.EntityBase;
import org.jax.mgi.mgd.api.model.acc.entities.Accession;
import org.jax.mgi.mgd.api.model.mgi.entities.Organism;
import org.jax.mgi.mgd.api.model.mgi.entities.User;

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
	private String cytogeneticoffset;
	
	@ApiModelProperty(value="organism of this marker")
	//@JsonIgnore
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_organism_key", referencedColumnName="_organism_key")
	private Organism organism;
	
	//need MRK_Status entity
	//@ApiModelProperty(value="Controlled vocabulary table for all Marker Statuses (approved, withdrawn)")
	//@JsonIgnore
	//@OneToOne(fetch=FetchType.EAGER)
	//@JoinColumn(name="_marker_status_key", referencedColumnName="_term_key")
	//private Term markerStatus;
	
	//need MRK_Types entity
	//@ApiModelProperty(value="Controlled vocabulary table for all Marker Types")
	//@JsonIgnore
	//@OneToOne(fetch=FetchType.EAGER)
	//@JoinColumn(name="_marker_type_key", referencedColumnName="_term_key")
	//private Term markerType;
	
	@JsonIgnore
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@JsonIgnore
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;
	
	@JsonIgnore
	@OneToMany(mappedBy="_object_key")
	@Where(clause="_mgitype_key = 2 AND preferred = 1")
	private List<Accession> accessionIDs;
}
