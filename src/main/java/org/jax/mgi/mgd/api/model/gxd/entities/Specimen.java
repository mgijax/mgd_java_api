package org.jax.mgi.mgd.api.model.gxd.entities;

import java.util.Date;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.voc.entities.Term;

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
@Schema(description = "Specimen Model Object")
@Table(name="gxd_specimen")
public class Specimen extends BaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="gxd_specimen_generator")
	@SequenceGenerator(name="gxd_specimen_generator", sequenceName = "gxd_specimen_seq", allocationSize=1)
	@Schema(name="primary key")	
	private int _specimen_key;
	private int _assay_key;
	private Integer sequenceNum;
	private String specimenLabel;
	private String sex;
	private String age;
	private Integer ageMin;
	private Integer ageMax;
	private String ageNote;
	private String hybridization;
	private String specimenNote;
	private Date creation_date;
	private Date modification_date;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_embedding_key", referencedColumnName="_term_key")
	private Term embeddingMethod;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_fixation_key", referencedColumnName="_term_key")
	private Term fixationMethod;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_genotype_key")
	private Genotype genotype;
	
	@OneToMany()
	@JoinColumn(name="_specimen_key", insertable=false, updatable=false)
	private List<InSituResult> results;
	
}
