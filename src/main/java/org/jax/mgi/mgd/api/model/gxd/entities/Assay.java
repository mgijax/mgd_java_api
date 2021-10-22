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

import org.hibernate.annotations.OrderBy;
import org.hibernate.annotations.Where;
import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.acc.entities.Accession;
import org.jax.mgi.mgd.api.model.bib.entities.Reference;
import org.jax.mgi.mgd.api.model.img.entities.ImagePane;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mrk.entities.Marker;
import org.jax.mgi.mgd.api.model.voc.entities.Term;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Assay Model Object")
@Table(name="gxd_assay")
public class Assay extends BaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="gxd_assay_generator")
	@SequenceGenerator(name="gxd_assay_generator", sequenceName = "gxd_assay_seq", allocationSize=1)
	@ApiModelProperty(value="primary key")	
	private int _assay_key;
	private int _imagepane_key;
	private Date creation_date;
	private Date modification_date;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_assaytype_key")
	private AssayType assayType;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_refs_key")
	private Reference reference;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_marker_key")
	private Marker marker;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_antibodyprep_key")
	private AntibodyPrep antibodyPrep;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_probeprep_key")
	private ProbePrep probePrep;

	// for DAO
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_imagepane_key")
	private ImagePane imagePane;
	
	// for display purposes only
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_imagepane_key")
	private GelImageView imagePaneDisplay;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_reportergene_key", referencedColumnName="_term_key")
	private Term reporterGene;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;
	
	// mgi accession ids only
	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_assay_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 8 and `_logicaldb_key` = 1")
	@OrderBy(clause="preferred desc, accID")
	private List<Accession> mgiAccessionIds;
	
	@OneToMany()
	@JoinColumn(name="_assay_key", insertable=false, updatable=false)
	private List<AssayNote> assayNote;
	
	@OneToMany()
	@JoinColumn(name="_assay_key", insertable=false, updatable=false)
	@OrderBy(clause="sequenceNum")
	private List<Specimen> specimens;
	
	@OneToMany()
	@JoinColumn(name="_assay_key", insertable=false, updatable=false)
	@OrderBy(clause="sequenceNum")
	private List<GelLane> gelLanes;

	@OneToMany()
	@JoinColumn(name="_assay_key", insertable=false, updatable=false)
	@OrderBy(clause="sequenceNum")
	private List<GelRow> gelRows;
		
}
