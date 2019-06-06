package org.jax.mgi.mgd.api.model.gxd.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.all.entities.Allele;
import org.jax.mgi.mgd.api.model.all.entities.AlleleCellLine;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mrk.entities.Marker;
import org.jax.mgi.mgd.api.model.voc.entities.Term;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "AllelePair Model Object")
@Table(name="gxd_allelepair")
public class AllelePair extends BaseEntity {

	@Id
	private int _allelepair_key;
	private Integer sequenceNum;
	private Date creation_date;
	private Date modification_date;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_genotype_key")
	private Genotype genotype;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_allele_key_1", referencedColumnName="_allele_key")
	private Allele allele1;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_allele_key_2", referencedColumnName="_allele_key")
	private Allele allele2;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_marker_key")
	private Marker marker;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_mutantcellline_key_1", referencedColumnName="_cellline_key")
	private AlleleCellLine cellLine1;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_mutantcellline_key_2", referencedColumnName="_cellline_key")
	private AlleleCellLine cellLine2;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_pairstate_key", referencedColumnName="_term_key")
	private Term pairState;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_compound_key", referencedColumnName="_term_key")
	private Term compound;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;
}
