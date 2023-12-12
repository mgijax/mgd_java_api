package org.jax.mgi.mgd.api.model.mgi.entities;

import java.util.Date;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.hibernate.annotations.Where;
import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.all.entities.AlleleCellLineDerivation;
import org.jax.mgi.mgd.api.model.all.entities.CellLine;
import org.jax.mgi.mgd.api.model.gxd.entities.HTSample;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeSource;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeStrain;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeTissue;
import org.jax.mgi.mgd.api.model.voc.entities.Term;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Schema(description = "MGI Translation  Object")
@Table(name="mgi_translation")
public class MGITranslation extends BaseEntity {
	@Id
	private Integer _translation_key;
	
	private Integer _object_key;
	private String badName;
	private Integer sequenceNum;
	private Date creation_date;
	private Date modification_date;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_translationType_key")
	private MGITranslationType translationType;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_object_key", referencedColumnName="_cellline_key", insertable=false, updatable=false)
	@Where(clause="translationType.`_mgitype_key` = 28")
	private CellLine cellLine;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_object_key", referencedColumnName="_derivation_key", insertable=false, updatable=false)
	@Where(clause="translationType.`_mgitype_key` = 36")
	private AlleleCellLineDerivation derivation;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_object_key", referencedColumnName="_sample_key", insertable=false, updatable=false)
	@Where(clause="translationType.`_mgitype_key` = 43")
	private HTSample htSample;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_object_key", referencedColumnName="_organism_key", insertable=false, updatable=false)
	@Where(clause="translationType.`_mgitype_key` = 20")
	private Organism organism;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_object_key", referencedColumnName="_source_key", insertable=false, updatable=false)
	@Where(clause="translationType.`_mgitype_key` = 5")
	private ProbeSource source;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_object_key", referencedColumnName="_strain_key", insertable=false, updatable=false)
	@Where(clause="translationType.`_mgitype_key` = 10")
	private ProbeStrain strain;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_object_key", referencedColumnName="_tissue_key", insertable=false, updatable=false)
	@Where(clause="translationType.`_mgitype_key` = 24")
	private ProbeTissue tissue;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_object_key", referencedColumnName="_term_key", insertable=false, updatable=false)
	@Where(clause="translationType.`_mgitype_key` = 13")
	private Term term;
}
