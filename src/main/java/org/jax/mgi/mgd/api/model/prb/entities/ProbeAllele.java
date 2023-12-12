package org.jax.mgi.mgd.api.model.prb.entities;

import java.util.Date;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.mgi.entities.User;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Schema(description = "Probe/Allele Model Object")
@Table(name="prb_allele")
public class ProbeAllele extends BaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="prb_allele_generator")
	@SequenceGenerator(name="prb_allele_generator", sequenceName = "prb_allele_seq", allocationSize=1)
	@Schema(name="primary key")
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
