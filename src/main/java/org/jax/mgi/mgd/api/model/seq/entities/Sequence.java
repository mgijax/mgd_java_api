package org.jax.mgi.mgd.api.model.seq.entities;

import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Where;
import org.jax.mgi.mgd.api.model.EntityBase;
import org.jax.mgi.mgd.api.model.map.entities.CoordinateFeature;
import org.jax.mgi.mgd.api.model.mgi.entities.Organism;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.entities.Term;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Sequence Model Object")
@Table(name="seq_sequence")
public class Sequence extends EntityBase {

	@Id
	private Integer _sequence_key;
	private Integer length;
	private String description;
	private String version;
	private String division;
	private Integer virtual;
	private Integer numberOfOrganisms;
	private Date seqrecord_date;
	private Date sequence_date;
	private Date creation_date;
	private Date modification_date;
	
	//@JsonIgnore
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_sequencetype_key", referencedColumnName="_term_key")
	private Term sequenceType;
	
	//@JsonIgnore
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_sequencequality_key", referencedColumnName="_term_key")
	private Term sequenceQuality;
	
	//@JsonIgnore
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_sequencestatus_key", referencedColumnName="_term_key")
	private Term sequenceStatus;
	
	//@JsonIgnore
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_sequenceprovider_key", referencedColumnName="_term_key")
	private Term sequenceProvider;
	
	//@JsonIgnore
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_organism_key")
	private Organism organism;
	
	//@JsonIgnore
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	//@JsonIgnore
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;
	
	@OneToMany(fetch=FetchType.EAGER)	@JoinColumn(name="_object_key", referencedColumnName="_sequence_key")
	@Where(clause="_mgitype_key = 19")
	private Set<CoordinateFeature> features;
}
