package org.jax.mgi.mgd.api.model.prb.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.gxd.entities.Genotype;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.entities.Term;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Probe Strain Genotype Model Object")
@Table(name="prb_strain_genotype")

public class ProbeStrainGenotype extends BaseEntity{
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="prb_strain_genotype_generator")
	@SequenceGenerator(name="prb_strain_genotype_generator", sequenceName = "prb_strain_genotype_seq", allocationSize=1)
	@ApiModelProperty(value="primary key")	
	private int _straingenotype_key;
	private int _strain_key;
	private Date creation_date;
	private Date modification_date;

	@OneToOne
	@JoinColumn(name="_genotype_key")
	private Genotype genotype;
	
	@OneToOne
	@JoinColumn(name="_qualifier_key", referencedColumnName="_term_key")
	private Term qualifier;
	
	@OneToOne
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;
}
