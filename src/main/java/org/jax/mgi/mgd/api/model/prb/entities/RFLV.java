package org.jax.mgi.mgd.api.model.prb.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mrk.entities.Marker;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "RFLV Model Object")
@Table(name="prb_rflv")
public class RFLV extends BaseEntity {

	@Id
	private int _rflv_key;
	private String endonuclease;
	private Date creation_date;
	private Date modification_date;
	
	@OneToOne
	@JoinColumn(name="_reference_key")
	private ProbeReference reference;

	@OneToOne
	@JoinColumn(name="_marker_key")
	private Marker marker;

	@OneToOne
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;
}
