package org.jax.mgi.mgd.api.model.gxd.entities;

import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Where;
import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.mgi.entities.MGIProperty;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeSource;
import org.jax.mgi.mgd.api.model.voc.entities.Term;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "HTExperiment Model Object")
@Table(name="gxd_htexperiment")
public class HTExperiment extends BaseEntity {

	@Id
	private Integer _experiment_key;
	private String name;
	private String description;
	private Date release_date;
	private Date lastupdate_date;
	private Date evaluated_date;
	private Date initial_date;
	private Date last_curated_date;
	private Date creation_date;
	private Date modification_date;
	
	@OneToOne
	@JoinColumn(name="_source_key")
	private ProbeSource source;
	
	@OneToOne
	@JoinColumn(name="_evaluationstate_key", referencedColumnName="_term_key")
	private Term evaluationState;
	
	@OneToOne
	@JoinColumn(name="_curationstate_key", referencedColumnName="_term_key")
	private Term curationState;
	
	@OneToOne
	@JoinColumn(name="_studytype_key", referencedColumnName="_term_key")
	private Term studyType;
	
	@OneToOne
	@JoinColumn(name="_experimenttype_key", referencedColumnName="_term_key")
	private Term experimentType;
	
	@OneToOne
	@JoinColumn(name="_evaluatedby_key", referencedColumnName="_term_key")
	private Term evaluatedBy;
	
	@OneToOne
	@JoinColumn(name="_initialcuratedby_key", referencedColumnName="_term_key")
	private Term initialCuratedBy;
	
	@OneToOne
	@JoinColumn(name="_lastcuratedby_key", referencedColumnName="_term_key")
	private Term lastCuratedBy;
	
	@OneToOne
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;
	
	@OneToMany
	@JoinColumn(name="_object_key", referencedColumnName="_experiment_key")
	@Where(clause="`_mgitype_key` = 42")
	private Set<MGIProperty> properties;
	
}
