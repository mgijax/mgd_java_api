package org.jax.mgi.mgd.api.model.crs.entities;

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
@ApiModel(value = "Cross Model Object")
@Table(name="crs_cross")
public class Cross extends EntityBase {

	@Id
	private Integer _cross_key;
	private String type;
	private String femaleallele1;
	private String femaleallele2;
	private String maleallele1;
	private String maleallele2;
	private String abbrevho;
	private String abrevht;
	private String whosecross;
	private Integer allelefromsegparent;
	private Integer f1directionknown;
	private Integer nprogeny;
	private Integer displayed;
	private Date creation_date;
	private Date modification_date;
	
}
