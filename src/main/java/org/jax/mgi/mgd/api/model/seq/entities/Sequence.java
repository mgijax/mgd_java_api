package org.jax.mgi.mgd.api.model.seq.entities;

import java.util.Date;
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

import org.hibernate.annotations.Where;
import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.map.entities.CoordinateFeature;
import org.jax.mgi.mgd.api.model.mgi.entities.Organism;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeSource;
import org.jax.mgi.mgd.api.model.voc.entities.Term;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Sequence Model Object")
@Table(name="seq_sequence")
public class Sequence extends BaseEntity {

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
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_sequencetype_key", referencedColumnName="_term_key")
	private Term sequenceType;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_sequencequality_key", referencedColumnName="_term_key")
	private Term sequenceQuality;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_sequencestatus_key", referencedColumnName="_term_key")
	private Term sequenceStatus;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_sequenceprovider_key", referencedColumnName="_term_key")
	private Term sequenceProvider;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_organism_key")
	private Organism organism;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;
	
	@OneToMany
	@JoinColumn(name="_sequence_key")
	private Set<SequenceMarkerCache> sequenceMarkers;
	
	@OneToMany
	@JoinColumn(name="_marker_key")
	private Set<SequenceMarkerCache> sequences;
	
	@OneToMany
	@JoinColumn(name="_object_key", referencedColumnName="_sequence_key")
	@Where(clause="`_mgitype_key` = 19")
	private Set<CoordinateFeature> features;
	
	@ManyToMany
	@JoinTable(name = "seq_source_assoc",
		joinColumns = @JoinColumn(name = "_sequence_key"),
		inverseJoinColumns = @JoinColumn(name = "_source_key")
	)
	private Set<ProbeSource> probeSources;
}
