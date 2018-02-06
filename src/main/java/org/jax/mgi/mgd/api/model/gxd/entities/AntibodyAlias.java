package org.jax.mgi.mgd.api.model.gxd.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.EntityBase;
import org.jax.mgi.mgd.api.model.bib.entities.Reference;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Antibody Alias Model Object")
@Table(name="gxd_antibodyalias")
public class AntibodyAlias extends EntityBase {

	@Id
	private Integer _antibodyalias_key;
	private String alias;
	private Date creation_date;
	private Date modification_date;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_antibody_key")
	private Antibody antibody;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_refs_key")
	private Reference reference;
	
}
