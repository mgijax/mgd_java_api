package org.jax.mgi.mgd.api.model.acc.entities;

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
@ApiModel(value = "AccessionMax Model Object")
@Table(name="acc_accessionmax")
public class AccessionMax extends EntityBase {
	@Id
	private String prefixPart;
	private Integer maxNumericPart;
	private Date creation_date;
	private Date modification_date;
}
