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
@ApiModel(value = "Pattern Model Object")
@Table(name="gxd_pattern")
public class Pattern extends BaseEntity {

	@Id
	private Integer _pattern_key;
	private String pattern;
	private Date creation_date;
	private Date modification_date;
}
