package org.jax.mgi.mgd.api.model.gxd.entities;

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
@ApiModel(value = "GelRNAType Model Object")
@Table(name="gxd_gelrnatype")
public class GelRNAType extends BaseEntity {

	@Id
	private Integer _gelrnatype_key;
	private String rnaType;
	private Date creation_date;
	private Date modification_date;
}
