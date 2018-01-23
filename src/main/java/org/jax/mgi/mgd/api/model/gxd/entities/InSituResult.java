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
@ApiModel(value = "InSituResult Model Object")
@Table(name="gxd_insituresult")
public class InSituResult extends EntityBase {

	@Id
	private Integer _result_key;
	private Integer sequenceNum;
	private String resultNote;
	private Date creation_date;
	private Date modification_date;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_specimen_key", referencedColumnName="_specimen_key")
	private Specimen specimen;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_strength_key", referencedColumnName="_strength_key")
	private Strength strength;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_pattern_key", referencedColumnName="_pattern_key")
	private Pattern pattern;
	
}
