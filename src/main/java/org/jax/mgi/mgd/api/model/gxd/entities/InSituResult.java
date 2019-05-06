package org.jax.mgi.mgd.api.model.gxd.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.BaseEntity;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "InSituResult Model Object")
@Table(name="gxd_insituresult")
public class InSituResult extends BaseEntity {

	@Id
	private Integer _result_key;
	private Integer sequenceNum;
	private String resultNote;
	private Date creation_date;
	private Date modification_date;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_strength_key")
	private Strength strength;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_pattern_key")
	private Pattern pattern;

	@OneToMany()
	@JoinColumn(name="_result_key")
	private List<InSituResultStructure> structures;
	
	}
