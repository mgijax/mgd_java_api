package org.jax.mgi.mgd.api.model.gxd.entities;

import java.util.Date;
import java.util.Set;

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
@ApiModel(value = "Gel Lane Model Object")
@Table(name="gxd_gellane")
public class GelLane extends BaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="gxd_gellane_generator")
	@SequenceGenerator(name="gxd_gellane_generator", sequenceName = "gxd_gellane_seq", allocationSize=1)
	@ApiModelProperty(value="primary key")		
	private int _gellane_key;
	private int _assay_key;
	private Integer sequenceNum;
	private String laneLabel;
	private String sampleAmount;
	private String sex;
	private String age;
	private Integer ageMin;
	private Integer ageMax;
	private String ageNote;
	private String laneNote;
	private Date creation_date;
	private Date modification_date;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_genotype_key")
	private Genotype genotype;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_gelrnatype_key", referencedColumnName="_term_key")
	private Term gelRNAType;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_gelcontrol_key", referencedColumnName="_term_key")
	private Term gelControl;
	
	@OneToMany()
	@JoinColumn(name="_gellane_key", insertable=false, updatable=false)
	private Set<GelLaneStructure> structures;

	@OneToMany()
	@JoinColumn(name="_gellane_key", insertable=false, updatable=false)
	private Set<GelBand> gelBands;
	
}
