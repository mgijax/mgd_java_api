package org.jax.mgi.mgd.api.model.ri.entities;

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
@ApiModel(value = "RI_Summary Model Object")
@Table(name="ri_summary")
public class Summary extends EntityBase {

	@Id
	private Integer _risummary_key;
	private String summary;
	private Date creation_date;
	private Date modification_date;
}
