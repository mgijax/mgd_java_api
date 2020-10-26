package org.jax.mgi.mgd.api.model.gxd.entities;

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
@ApiModel(value = "Pattern Model Object")
@Table(name="gxd_pattern")
public class Pattern extends BaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="gxd_pattern_generator")
	@SequenceGenerator(name="gxd_pattern_generator", sequenceName = "gxd_pattern_seq", allocationSize=1)
	@ApiModelProperty(value="primary key")	
	private int _pattern_key;
	private String pattern;
	private Date creation_date;
	private Date modification_date;
}
