package org.jax.mgi.mgd.api.model.gxd.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.EntityBase;
import org.jax.mgi.mgd.api.model.mgi.entities.Organism;
import org.jax.mgi.mgd.api.model.voc.entities.Term;
import org.jax.mgi.mgd.api.model.mgi.entities.User;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "HTSample Model Object")
@Table(name="gxd_htsample")
public class HTSample extends EntityBase {

	@Id
	private Integer _sample_key;
	private String name;
	private String age;
	private Date creation_date;
	private Date modification_date;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_experiment_key")
	private HTExperiment experiment;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_organism_key")
	private Organism organism;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_relevance_key", referencedColumnName="_term_key")
	private Term relevance;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_sex_key", referencedColumnName="_term_key")
	private Term sex;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_emapa_key", referencedColumnName="_term_key")
	private Term emapa;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_stage_key", referencedColumnName="_stage_key")
	private TheilerStage stage;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_genotype_key")
	private Genotype genotype;
	
	@JsonIgnore
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@JsonIgnore
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;
}
