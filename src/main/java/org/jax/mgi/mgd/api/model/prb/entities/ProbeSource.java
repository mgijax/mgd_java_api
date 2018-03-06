package org.jax.mgi.mgd.api.model.prb.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.all.entities.AlleleCellLine;
import org.jax.mgi.mgd.api.model.bib.entities.Reference;
import org.jax.mgi.mgd.api.model.mgi.entities.Organism;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.entities.Term;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "PRBSource Model Object")
@Table(name="prb_source")
public class ProbeSource extends BaseEntity {

	@Id
	private Integer _source_key;
	private String name;
	private String description;
	private String age;
	private Integer ageMin;
	private Integer ageMax;
	private Integer isCuratorEdited;
	private Date creation_date;
	private Date modification_date;
	
	@OneToOne
	@JoinColumn(name="_segmenttype_key", referencedColumnName="_term_key")
	private Term segmentType;

	@OneToOne
	@JoinColumn(name="_vector_key", referencedColumnName="_term_key")
	private Term vector;

	@OneToOne
	@JoinColumn(name="_organism_key")
	private Organism organism;

	@OneToOne
	@JoinColumn(name="_strain_key")
	private ProbeStrain strain;

	@OneToOne
	@JoinColumn(name="_tissue_key")
	private ProbeTissue tissue;

	@OneToOne
	@JoinColumn(name="_gender_key", referencedColumnName="_term_key")
	private Term gender;

	@OneToOne
	@JoinColumn(name="_cellline_key")
	private AlleleCellLine cellLine;

	@OneToOne
	@JoinColumn(name="_refs_key")
	private Reference reference;

	@OneToOne
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;
}
