package org.jax.mgi.mgd.api.model.prb.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.EntityBase;
import org.jax.mgi.mgd.api.model.all.entities.Allele;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mrk.entities.Marker;
import org.jax.mgi.mgd.api.model.voc.entities.Term;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Probe Strain Marker Model Object")
@Table(name="prb_strain_marker")

public class ProbeStrainMarker extends EntityBase{
	@Id
	private Integer _strainmarker_key;
	private Date creation_date;
	private Date modification_date;
	
	@OneToOne
	@JoinColumn(name="_strain_key")
	private ProbeStrain strain;
	
	@OneToOne
	@JoinColumn(name="_marke_key")
	private Marker marker;
	
	@OneToOne
	@JoinColumn(name="_allele_key")
	private Allele allele;
	
	@OneToOne
	@JoinColumn(name="_qualifer_key", referencedColumnName="_term_key")
	private Term qualifier;
	
	@OneToOne
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;
}
