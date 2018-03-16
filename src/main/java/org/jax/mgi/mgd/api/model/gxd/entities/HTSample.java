package org.jax.mgi.mgd.api.model.gxd.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.mgi.entities.Organism;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.entities.Term;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "HTSample Model Object")
@Table(name="gxd_htsample")
public class HTSample extends BaseEntity {

	@Id
	private Integer _sample_key;
	private String name;
	private String age;
	private Date creation_date;
	private Date modification_date;
	
	@OneToOne
	@JoinColumn(name="_experiment_key")
	private HTExperiment experiment;
	
	@OneToOne
	@JoinColumn(name="_organism_key")
	private Organism organism;
	
	@OneToOne
	@JoinColumn(name="_relevance_key", referencedColumnName="_term_key")
	private Term relevance;
	
	@OneToOne
	@JoinColumn(name="_sex_key", referencedColumnName="_term_key")
	private Term sex;
	
	@OneToOne
	@JoinColumn(name="_emapa_key", referencedColumnName="_term_key")
	private Term emapa;
	
	@OneToOne
	@JoinColumn(name="_stage_key", referencedColumnName="_stage_key")
	private TheilerStage stage;
	
	@OneToOne
	@JoinColumn(name="_genotype_key")
	private Genotype genotype;
	
	@OneToOne
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;
}
