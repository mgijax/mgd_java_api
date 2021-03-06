package org.jax.mgi.mgd.api.model.mld.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.BaseEntity;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Hybrid Model Object")
@Table(name="mld_hybrid")
public class Hybrid extends BaseEntity {

	@Id
	private Integer _expt_key;
	private Integer chrsOrGenes;
	private String band;
	private Date creation_date;
	private Date modification_date;
}
