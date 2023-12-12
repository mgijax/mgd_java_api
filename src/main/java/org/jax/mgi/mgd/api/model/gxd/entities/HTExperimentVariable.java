package org.jax.mgi.mgd.api.model.gxd.entities;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.voc.entities.Term;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Schema(description = "HT Experiment Variable Model Object")
@Table(name="gxd_htexperimentvariable")
public class HTExperimentVariable extends BaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="gxd_htexperimentvariable_generator")
	@SequenceGenerator(name="gxd_htexperimentvariable_generator", sequenceName = "gxd_htexperimentvariable_seq", allocationSize=1)
	@Schema(name="primary key")	
	private Integer _experimentvariable_key;
	private Integer _experiment_key;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_term_key", referencedColumnName="_term_key")
	private Term term;

}
