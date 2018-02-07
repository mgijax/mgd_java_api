package org.jax.mgi.mgd.api.model.prb.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.EntityBase;
import org.jax.mgi.mgd.api.model.bib.entities.Reference;
import org.jax.mgi.mgd.api.model.mgi.entities.User;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "PRBReference Model Object")
@Table(name="prb_reference")
public class ProbeReference extends EntityBase {

	@Id
	private Integer _reference_key;
	private Integer hasRmap;
	private Integer hasSequence;
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
