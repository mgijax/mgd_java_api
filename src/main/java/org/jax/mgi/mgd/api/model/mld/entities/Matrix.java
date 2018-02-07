package org.jax.mgi.mgd.api.model.mld.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.EntityBase;
import org.jax.mgi.mgd.api.model.crs.entities.Cross;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "MLD Matrix Model Object")
@Table(name="mld_matrix")
public class Matrix extends EntityBase {

	@Id
	private Integer _expt_key;
	private String female;
	private String female2;
	private String male;
	private String male2;
	private Date creation_date;
	private Date modification_date;
	
	@OneToOne
	@JoinColumn(name="_cross_key")
	private Cross cross;
	
}
