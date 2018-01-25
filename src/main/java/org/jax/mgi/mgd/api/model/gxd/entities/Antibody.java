package org.jax.mgi.mgd.api.model.gxd.entities;

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
import org.jax.mgi.mgd.api.model.mgi.entities.Organism;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mrk.entities.Marker;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Antibody Model Object")
@Table(name="gxd_antibody")
public class Antibody extends EntityBase {

	@Id
	private Integer _antibody_key;
	private String antibodyName;
	private String antibodyNote;
	private Date creation_date;
	private Date modification_date;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_antibodyclass_key", referencedColumnName="_antibodyclass_key")
	private AntibodyClass antibodyClass;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_antibodytype_key", referencedColumnName="_antibodytype_key")
	private AntibodyType antibodyType;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_organism_key", referencedColumnName="_organism_key")
	private Organism organism;
	
	@JsonIgnore
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;
	
	@JsonIgnore
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_antibody_key", referencedColumnName="_object_key")
	@Where(clause="_mgitype_key = 6 AND preferred = 1 AND _logicaldb_key = 1")
	private Accession mgiAccessionId;

	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name = "gxd_antibody_marker",
		joinColumns = @JoinColumn(name = "_antibody_key"),
		inverseJoinColumns = @JoinColumn(name = "_marker_key")
	)
	private Set<Marker> markers;
	
}
