package org.jax.mgi.mgd.api.model.crs.entities;

import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeStrain;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Cross Model Object")
@Table(name="crs_cross")
public class Cross extends BaseEntity {

	@Id
	private int _cross_key;
	private String type;
	private String femaleAllele1;
	private String femaleAllele2;
	private String maleAllele1;
	private String maleAllele2;
	private String abbrevHO;
	private String abrevHT;
	private String whoseCross;
	private int alleleFromSegParent;
	private int F1DirectionKnown;
	private int nProgeny;
	private int displayed;
	private Date creation_date;
	private Date modification_date;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_femalestrain_key", referencedColumnName="_strain_key")
	private ProbeStrain femaleStrain;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_malestrain_key", referencedColumnName="_strain_key")
	private ProbeStrain maleStrain;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_strainho_key", referencedColumnName="_strain_key")
	private ProbeStrain homozygousStrain;
	 
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_strainht_key", referencedColumnName="_strain_key")
	private ProbeStrain heterozygousStrain;
	
	@OneToMany
	@JoinColumn(name="_cross_key")
	@OrderColumn(name="rowNumber")
	private Set<CrossMatrix> matrices;
	
	@OneToMany
	@JoinColumn(name="_cross_key")
	private Set<CrossReference> crossReferences;
	
	@OneToMany
	@JoinColumn(name="_cross_key")
	//@OrderColumn(name="rowNumber,colNumber")
	private Set<CrossTyping> crossTypings;
	
	@OneToMany
	@JoinColumn(name="_cross_key")
	@OrderColumn(name="rowNumber")
	private Set<CrossProgeny> properties;
}
