package org.jax.mgi.mgd.api.model.gxd.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.voc.entities.Term;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Specimen Model Object")
@Table(name="gxd_specimen")
public class Specimen extends BaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="gxd_specimen_generator")
	@SequenceGenerator(name="gxd_specimen_generator", sequenceName = "gxd_specimen_seq", allocationSize=1)
	@ApiModelProperty(value="primary key")	
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
