package org.jax.mgi.mgd.api.model.prb.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.EntityBase;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Strain Model Object")
@Table(name="prb_strain")
public class Strain extends EntityBase {

	@Id
	private Integer _strain_key;
	private String strain;
	private Integer standard;

	@Column(name="private") // just "private" is a Java reserved word
	private Integer is_private;

	private Integer geneticBackground;
	private Date creation_date;
	private Date modification_date;
}
