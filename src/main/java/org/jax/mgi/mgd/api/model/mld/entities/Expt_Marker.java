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
@ApiModel(value = "Expt_Marker Model Object")
@Table(name="mld_expt_marker")
public class Expt_Marker extends EntityBase {

	@Id
	private Integer _expt_key;
	private Integer sequenceNum;
	private String gene;
	private String description;
	private Integer matrixData;
	private Date creation_date;
	private Date modification_date;
}
