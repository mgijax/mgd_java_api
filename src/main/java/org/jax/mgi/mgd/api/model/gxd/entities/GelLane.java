package org.jax.mgi.mgd.api.model.gxd.entities;

import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.jax.mgi.mgd.api.model.EntityBase;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "GelLane Model Object")
@Table(name="gxd_gellane")
public class GelLane extends EntityBase {

	@Id
	private Integer _gellane_key;
	private Integer sequenceNum;
	private String lanelLabel;
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
	@JoinColumn(name="_assay_key")
	private Assay assay;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_genotype_key")
	private Genotype genotype;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_gelrnatype_key")
	private GelRNAType gelRNAType;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_gelcontrol_key")
	private GelControl gelControl;
	
	@OneToMany(fetch=FetchType.LAZY)
	@JoinColumn(name="_gellane_key")
	private Set<GelLaneStructure> structures;
	
}
