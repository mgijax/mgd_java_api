package org.jax.mgi.mgd.api.model.mld.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.EntityBase;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "RI2Point Model Object")
@Table(name="mld_ri2point")
public class RI2Point extends EntityBase {

	@Id
	private Integer _expt_key;
	private Integer sequenceNum;
	private Integer numRecombinants;
	private Integer numTotal;
	private String RI_Lines;
	private Date creation_date;
	private Date modification_date;
}
