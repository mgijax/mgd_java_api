package org.jax.mgi.mgd.api.model.gxd.entities;

import java.util.Date;

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
import org.jax.mgi.mgd.api.model.prb.entities.Probe;
import org.jax.mgi.mgd.api.model.voc.entities.Term;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "ProbePrep Model Object")
@Table(name="gxd_probeprep")
public class ProbePrep extends BaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="gxd_probeprep_generator")
	@SequenceGenerator(name="gxd_probeprep_generator", sequenceName = "gxd_probeprep_seq", allocationSize=1)
	@ApiModelProperty(value="primary key")		
	private int _probeprep_key;
	private String type;
	private Date creation_date;
	private Date modification_date;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_probe_key")
	private Probe probe;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_sense_key", referencedColumnName="_term_key")
	private Term probeSense;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_label_key", referencedColumnName="_term_key")
	private Term label;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_visualization_key", referencedColumnName="_term_key")
	private Term visualizationMethod;
	
}
