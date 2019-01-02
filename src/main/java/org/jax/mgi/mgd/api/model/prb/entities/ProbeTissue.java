package org.jax.mgi.mgd.api.model.prb.entities;

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
@ApiModel(value = "Tissue Model Object")
@Table(name="prb_tissue")
public class ProbeTissue extends BaseEntity {

	@Id
	private int _tissue_key;
	private String tissue;
	private int standard;
	private Date creation_date;
	private Date modification_date;
}
