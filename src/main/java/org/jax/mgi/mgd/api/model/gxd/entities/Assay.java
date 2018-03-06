package org.jax.mgi.mgd.api.model.gxd.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
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
	
	@OneToOne
	@JoinColumn(name="_assaytype_key")
	private AssayType assayType;

	@OneToOne
	@JoinColumn(name="_refs_key")
	private Reference reference;

	@OneToOne
	@JoinColumn(name="_marker_key")
	private Marker marker;

	@OneToOne
	@JoinColumn(name="_probeprep_key")
	private ProbePrep probePrep;

	@OneToOne
	@JoinColumn(name="_antibodyprep_key")
	private AntibodyPrep antibodyPrep;

	@OneToOne
	@JoinColumn(name="_imagepane_key")
	private ImagePane imagePane;

	@OneToOne
	@JoinColumn(name="_reportergene_key", referencedColumnName="_term_key")
	private Term reporterGene;

	@OneToOne
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;
	
	@OneToOne
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;
	
	@OneToOne
	@JoinColumn(name="_assay_key", referencedColumnName="_object_key")
	@Where(clause="`_mgitype_key` = 8 AND preferred = 1 AND `_logicaldb_key` = 1")
	private Accession mgiAccessionId;

}
