package org.jax.mgi.mgd.api.model.mgi.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Where;
import org.jax.mgi.mgd.api.model.EntityBase;
import org.jax.mgi.mgd.api.model.all.entities.CellLine;
import org.jax.mgi.mgd.api.model.all.entities.CellLineDerivation;
import org.jax.mgi.mgd.api.model.gxd.entities.HTSample;
import org.jax.mgi.mgd.api.model.prb.entities.Source;
import org.jax.mgi.mgd.api.model.prb.entities.Strain;
import org.jax.mgi.mgd.api.model.prb.entities.Tissue;
import org.jax.mgi.mgd.api.model.voc.entities.Term;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "MGI Translation  Object")
@Table(name="mgi_translation")
public class MGITranslation extends EntityBase {
	@Id
	private Integer _translation_key;
	
	private Integer _object_key;
	private String badName;
	private Integer sequenceNum;
	private Date creation_date;
	private Date modification_date;

	
	//@JsonIgnore
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	//@JsonIgnore
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_translationType_key")
	private MGITranslationType translationType;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_object_key", referencedColumnName="_cellline_key", insertable=false, updatable=false)
	@Where(clause="translationType._mgitype_key = 28")
	private CellLine cellLine;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_object_key", referencedColumnName="_derivation_key", insertable=false, updatable=false)
	@Where(clause="translationType._mgitype_key = 36")
	private CellLineDerivation derivation;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_object_key", referencedColumnName="_sample_key", insertable=false, updatable=false)
	@Where(clause="translationType._mgitype_key = 43")
	private HTSample htSample;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_object_key", referencedColumnName="_organism_key", insertable=false, updatable=false)
	@Where(clause="translationType._mgitype_key = 20")
	private Organism organism;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_object_key", referencedColumnName="_source_key", insertable=false, updatable=false)
	@Where(clause="translationType._mgitype_key = 5")
	private Source source;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_object_key", referencedColumnName="_strain_key", insertable=false, updatable=false)
	@Where(clause="translationType._mgitype_key = 10")
	private Strain strain;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_object_key", referencedColumnName="_tissue_key", insertable=false, updatable=false)
	@Where(clause="translationType._mgitype_key = 24")
	private Tissue tissue;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="_object_key", referencedColumnName="_term_key", insertable=false, updatable=false)
	@Where(clause="translationType._mgitype_key = 13")
	private Term term;
}
