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
@ApiModel(value = "Alias Model Object")
@Table(name="mrk_alias")
public class Alias extends EntityBase {

	@Id
	private Integer _alias_key;
	private Date creation_date;
	private Date modification_date;
}
