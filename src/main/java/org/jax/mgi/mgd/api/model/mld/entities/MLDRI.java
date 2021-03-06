package org.jax.mgi.mgd.api.model.mld.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.ri.entities.RISet;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "RI Model Object")
@Table(name="mld_ri")
public class MLDRI extends BaseEntity {

	@Id
	private Integer _expt_key;
	private String RI_IdList;
	private Date creation_date;
	private Date modification_date;
	
	@OneToOne
	@JoinColumn(name="_riset_key")
	private RISet RISet;
	
}
