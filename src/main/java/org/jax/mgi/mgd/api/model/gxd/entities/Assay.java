package org.jax.mgi.mgd.api.model.gxd.entities;

import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Where;
import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.acc.entities.Accession;
import org.jax.mgi.mgd.api.model.bib.entities.Reference;
import org.jax.mgi.mgd.api.model.img.entities.ImagePane;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mrk.entities.Marker;
import org.jax.mgi.mgd.api.model.voc.entities.Term;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Assay Model Object")
@Table(name="gxd_assay")
public class Assay extends BaseEntity {

	@Id
	private Integer _assay_key;
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
	@JoinColumn(name="_probeprep_key")
	private ProbePrep probePrep;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_antibodyprep_key")
	private AntibodyPrep antibodyPrep;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_imagepane_key")
	private ImagePane imagePane;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_reportergene_key", referencedColumnName="_term_key")
	private Term reporterGene;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_assay_key", referencedColumnName="_object_key")
	@Where(clause="`_mgitype_key` = 8 AND preferred = 1 AND `_logicaldb_key` = 1")
	private Accession mgiAccessionId;

	@OneToMany
	@JoinColumn(name="_assay_key")
	@OrderBy("sequenceNum")
	private Set<GelLane> gelLanes;
	
	@OneToMany
	@JoinColumn(name="_assay_key")
	@OrderBy("sequenceNum")
	private Set<Specimen> specimens;
}
