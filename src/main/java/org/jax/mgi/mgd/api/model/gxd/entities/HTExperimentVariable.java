package org.jax.mgi.mgd.api.model.gxd.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.voc.entities.Term;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "HT Experiment Variable Model Object")
@Table(name="gxd_htexperimentvariable")
public class HTExperimentVariable extends BaseEntity {

	@Id
	private Integer _experimentvariable_key;
	private Integer _experiment_key;
	private Integer _term_key;

	@OneToOne
	@JoinColumn(name="_term_key", referencedColumnName="_term_key", insertable=false, updatable=false)
	private Term term;

}
