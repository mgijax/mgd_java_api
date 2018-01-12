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
@ApiModel(value = "RIData Model Object")
@Table(name="mld_ridata")
public class RIData extends EntityBase {

	@Id
	private Integer _expt_key;
	private Integer sequenceNum;
	private String alleleLine;
	private Date creation_date;
	private Date modification_date;
}
