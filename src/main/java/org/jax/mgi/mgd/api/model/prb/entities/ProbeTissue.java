package org.jax.mgi.mgd.api.model.prb.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.BaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Tissue Model Object")
@Table(name="prb_tissue")
public class ProbeTissue extends BaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="prb_tissue_generator")
    @SequenceGenerator(name="prb_tissue_generator", sequenceName = "prb_tissue_seq", allocationSize=1)
    @ApiModelProperty(value="primary key")

	private int _tissue_key;
	private String tissue;
	private int standard;
	private Date creation_date;
	private Date modification_date;
}
