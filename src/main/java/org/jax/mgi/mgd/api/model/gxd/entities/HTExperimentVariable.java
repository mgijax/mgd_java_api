package org.jax.mgi.mgd.api.model.gxd.entities;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.voc.entities.Term;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "HT Experiment Variable Model Object")
@Table(name="gxd_htexperimentvariable")
public class HTExperimentVariable extends BaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="gxd_htexperimentvariable_generator")
	@SequenceGenerator(name="gxd_htexperimentvariable_generator", sequenceName = "gxd_htexperimentvariable_seq", allocationSize=1)
	@ApiModelProperty(value="primary key")	
	private Integer _experimentvariable_key;
	private Integer _experiment_key;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_term_key", referencedColumnName="_term_key")
	private Term term;

}
