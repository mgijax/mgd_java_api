package org.jax.mgi.mgd.api.model.gxd.entities;

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
@ApiModel(value = "GelRow Model Object")
@Table(name="gxd_gelrow")
public class GelRow extends EntityBase {

	@Id
	private Integer _gelrow_key;
	private Integer sequenceNum;
	private Integer size;
	private String rowNote;
	private Date creation_date;
	private Date modification_date;
}
