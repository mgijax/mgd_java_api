package org.jax.mgi.mgd.api.model.mgi.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.EntityBase;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "MGI Statistic Object")
@Table(name="mgi_statistic")
public class MGIStatistic extends EntityBase {
	@Id
	private Integer _statistic_key;
	private String name;
	private String abbreviation;
	private String definition;
	private Integer isPrivate;
	private Integer hasIntValue;
	
	
}
