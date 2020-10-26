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
@ApiModel(value = "FixationMethod Model Object")
@Table(name="gxd_fixationmethod")
public class FixationMethod extends BaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="gxd_fixation_generator")
	@SequenceGenerator(name="gxd_fixation_generator", sequenceName = "gxd_fixation_seq", allocationSize=1)
	@ApiModelProperty(value="primary key")	
	private int _fixation_key;	
	private String fixation;
	private Date creation_date;
	private Date modification_date;
}
