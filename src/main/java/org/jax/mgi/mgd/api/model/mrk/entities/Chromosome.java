package org.jax.mgi.mgd.api.model.mrk.entities;

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
@ApiModel(value = "Chromosome Model Object")
@Table(name="mrk_chromosome")
public class Chromosome extends EntityBase {

	@Id
	private Integer _chromosome_key;
	private String chromosome;
	private Integer sequenceNum;
	private Date creation_date;
	private Date modification_date;
}
