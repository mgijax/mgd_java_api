package org.jax.mgi.mgd.api.model.crs.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.EntityBase;
import org.jax.mgi.mgd.api.model.prb.entities.Strain;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Cross Model Object")
@Table(name="crs_cross")
public class Cross extends EntityBase {

	@Id
	private Integer _cross_key;
	private String type;
	private String femaleAllele1;
	private String femaleAllele2;
	private String maleAllele1;
	private String maleAllele2;
	private String abbrevHO;
	private String abrevHT;
	private String whoseCross;
	private Integer alleleFromSegParent;
	private Integer F1DirectionKnown;
	private Integer nProgeny;
	private Integer displayed;
	private Date creation_date;
	private Date modification_date;
	
	//@JsonIgnore
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_femalestrain_key", referencedColumnName="_strain_key")
	private Strain femaleStrain;
	
	//@JsonIgnore
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_malestrain_key", referencedColumnName="_strain_key")
	private Strain maleStrain;
	
	//@JsonIgnore
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_strainho_key", referencedColumnName="_strain_key")
	private Strain homozygousStrain;
	 
	//@JsonIgnore
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_strainht_key", referencedColumnName="_strain_key")
	private Strain heterozygousStrain;
	
	
}
