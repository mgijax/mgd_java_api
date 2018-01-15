package org.jax.mgi.mgd.api.model.seq.entities;

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
@ApiModel(value = "GeneTrap Model Object")
@Table(name="seq_genetrap")
public class GeneTrap extends EntityBase {

	@Id
	private Integer _sequence_key;
	private Integer goodHitCount;
	private Integer pointCoordinate;
	private Date creation_date;
	private Date modification_date;
}
