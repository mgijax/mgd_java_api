package org.jax.mgi.mgd.api.model.gxd.entities;

import java.util.Date;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.all.entities.Allele;
import org.jax.mgi.mgd.api.model.all.entities.CellLine;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mrk.entities.Marker;
import org.jax.mgi.mgd.api.model.voc.entities.Term;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Schema(description = "AllelePair Model Object")
@Table(name="gxd_allelepair")
public class AllelePair extends BaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="gxd_allelepair_generator")
	@SequenceGenerator(name="gxd_allelepair_generator", sequenceName = "gxd_allelepair_seq", allocationSize=1)
	@Schema(name="primary key")	
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
	private CellLine cellLine1;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_mutantcellline_key_2", referencedColumnName="_cellline_key")
	private CellLine cellLine2;

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
