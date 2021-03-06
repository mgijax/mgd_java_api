package org.jax.mgi.mgd.api.model.mld.entities;

import java.util.Date;

import javax.persistence.Entity;
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
@ApiModel(value = "InSitu Model Object")
@Table(name="mld_insitu")
public class InSitu extends BaseEntity {

	@Id
	private Integer _expt_key;
	private String band;
	private String cellOrigin;
	private String karyotype;
	private String robertsonians;
	private Integer numMetaphase;
	private Integer totalGrains;
	private Integer grainsOnChrom;
	private Integer grainsOtherChrom;
	private Date creation_date;
	private Date modification_date;
	
	@OneToOne
	@JoinColumn(name="_strain_key")
	private ProbeStrain strain;
	
}
