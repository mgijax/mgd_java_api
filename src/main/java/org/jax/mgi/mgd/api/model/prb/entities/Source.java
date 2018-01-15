package org.jax.mgi.mgd.api.model.prb.entities;

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
@ApiModel(value = "PRBSource Model Object")
@Table(name="prb_source")
public class Source extends EntityBase {

	@Id
	private Integer _source_key;
	private String name;
	private String description;
	private String age;
	private Integer ageMin;
	private Integer ageMax;
	private Integer isCuratorEdited;
	private Date creation_date;
	private Date modification_date;
}
