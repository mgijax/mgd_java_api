package org.jax.mgi.mgd.api.model.gxd.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.EntityBase;
import org.jax.mgi.mgd.api.model.prb.entities.Probe;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "ProbePrep Model Object")
@Table(name="gxd_probeprep")
public class ProbePrep extends EntityBase {

	@Id
	private Integer _probeprep_key;
	private String type;
	private Date creation_date;
	private Date modification_date;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_probe_key", referencedColumnName="_probe_key")
	private Probe probe;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_sense_key", referencedColumnName="_sense_key")
	private ProbeSense probeSense;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_label_key", referencedColumnName="_label_key")
	private Label label;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_visualization_key", referencedColumnName="_visualization_key")
	private VisualizationMethod visualizationMethod;
	
}
