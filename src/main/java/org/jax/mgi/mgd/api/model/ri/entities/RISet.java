package org.jax.mgi.mgd.api.model.ri.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeStrain;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "RI_RISet Model Object")
@Table(name="ri_riset")
public class RISet extends BaseEntity {

	@Id
	private int _riset_key;
	private String designation;
	private String abbrev1;
	private String abbrev2;
	private String RI_IdList;
	private Date creation_date;
	private Date modification_date;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_strain_key_1", referencedColumnName="_strain_key")
	private ProbeStrain strain1;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_strain_key_2", referencedColumnName="_strain_key")
	private ProbeStrain strain2;
}
