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

import org.hibernate.annotations.OrderBy;
import org.hibernate.annotations.Where;
import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.acc.entities.Accession;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeStrain;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeStrainGenotype;
import org.jax.mgi.mgd.api.model.voc.entities.Term;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Genotype Model Object")
@Table(name="gxd_genotype")
public class Genotype extends BaseEntity {

	@Id
	private int _genotype_key;
	
	private Integer isConditional;
	private String note;
	private Date creation_date;
	private Date modification_date;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_strain_key")
	private ProbeStrain strain;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_existsas_key", referencedColumnName="_term_key")
	private Term existsAs;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;
	
	// mgi accession ids only
	@OneToMany
	@JoinColumn(name="_object_key", referencedColumnName="_genotype_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 12 and `_logicaldb_key` = 1")
	@OrderBy(clause="preferred desc, accID")
	private List<Accession> mgiAccessionIds;

	@OneToMany()
	@JoinColumn(name="_genotype_key")
	private List<ProbeStrainGenotype> probeStrainGenotypes;
	
}
