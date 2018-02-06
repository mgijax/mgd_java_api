package org.jax.mgi.mgd.api.model.gxd.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.EntityBase;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "AntibodyPrep Model Object")
@Table(name="gxd_antibodyprep")
public class AntibodyPrep extends EntityBase {

	@Id
	private Integer _antibodyprep_key;
	private Date creation_date;
	private Date modification_date;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_antibody_key")
	private Antibody antibody;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_secondary_key")
	private Secondary secondary;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_label_key")
	private GXDLabel label;
	
}
