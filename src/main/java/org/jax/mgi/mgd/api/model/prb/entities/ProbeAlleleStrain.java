package org.jax.mgi.mgd.api.model.prb.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.mgi.entities.User;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Probe/Allele/Strain Model Object")
@Table(name="prb_allele_strain")
public class ProbeAlleleStrain extends BaseEntity {

	// the primary key for this is _allele_key, _strain_key
	// since we are not editing this data anymore, we are leaving this as-is in the schema
	// if editing needs to be turned back on, them this will need to be converted to a single primary key
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="prb_allele_strain_generator")
	@SequenceGenerator(name="prb_allele_strain_generator", sequenceName = "prb_allele_strain_seq", allocationSize=1)
	@ApiModelProperty(value="primary key")
	private int _allelestrain_key;
	private int _allele_key;
	private Date creation_date;
	private Date modification_date;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_strain_key", referencedColumnName="_strain_key")
	private ProbeStrain strain;	
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;
	
}
