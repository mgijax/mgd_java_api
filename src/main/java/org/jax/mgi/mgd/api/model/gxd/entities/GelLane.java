package org.jax.mgi.mgd.api.model.gxd.entities;

import java.util.Date;
import java.util.Set;

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
@Schema(description = "Gel Lane Model Object")
@Table(name="gxd_gellane")
public class GelLane extends BaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="gxd_gellane_generator")
	@SequenceGenerator(name="gxd_gellane_generator", sequenceName = "gxd_gellane_seq", allocationSize=1)
	@Schema(name="primary key")		
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
