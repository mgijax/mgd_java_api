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
@ApiModel(value = "RI Model Object")
@Table(name="gxd_genotype")
public class RI extends EntityBase {

	@Id
	private Integer _expt_key;
	private String RI_IdList;
	private Date creation_date;
	private Date modification_date;
}
