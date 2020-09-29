package org.jax.mgi.mgd.api.model.prb.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.bib.entities.Reference;
import org.jax.mgi.mgd.api.model.mgi.entities.User;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "PRBReference Model Object")
@Table(name="prb_reference")
public class ProbeReference extends BaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="prb_reference_generator")
	@SequenceGenerator(name="prb_reference_generator", sequenceName = "prb_reference_seq", allocationSize=1)
	@ApiModelProperty(value="primary key")
	private int _reference_key;
	private int hasRmap;
	private int hasSequence;
	private Date creation_date;
	private Date modification_date;
	
	@OneToOne
	@JoinColumn(name="_probe_key")
	private Probe probe;

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
