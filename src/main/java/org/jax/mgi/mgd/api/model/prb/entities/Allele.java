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
@ApiModel(value = "PRBAllele Model Object")
@Table(name="prb_allele")
public class Allele extends EntityBase {

	@Id
	private Integer _allele_key;
	private String allele;
	private String fragments;
	private Date creation_date;
	private Date modification_date;
}
