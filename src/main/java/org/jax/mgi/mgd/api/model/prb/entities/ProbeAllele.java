package org.jax.mgi.mgd.api.model.prb.entities;

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
import org.jax.mgi.mgd.api.model.mgi.entities.User;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Probe/Allele Model Object")
@Table(name="prb_allele")
public class ProbeAllele extends BaseEntity {

	@Id
//	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="prb_allele_generator")
//	@SequenceGenerator(name="prb_allele_generator", sequenceName = "prb_allele_seq", allocationSize=1)
	@ApiModelProperty(value="primary key")
	private int _allele_key;
	private int _rflv_key;
	private String allele;
	private String fragments;
	private Date creation_date;
	private Date modification_date;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;
	
	// strains
	@OneToMany()
	@JoinColumn(name="_allele_key", referencedColumnName="_allele_key", insertable=false, updatable=false)
	private List<ProbeAlleleStrain> alleleStrains;
	
}
