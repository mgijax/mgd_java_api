package org.jax.mgi.mgd.api.model.mld.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.EntityBase;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeStrain;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "FISH Model Object")
@Table(name="mld_fish")
public class FISH extends EntityBase {

	@Id
	private Integer _expt_key;
	private String band;
	private String cellOrigin;
	private String karyotype;
	private String robertsonians;
	private String label;
	private Integer numMetaphase;
	private Integer totalSingle;
	private Integer totalDouble;
	private Date creation_date;
	private Date modification_date;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_strain_key")
	private ProbeStrain strain;
	
}
