package org.jax.mgi.mgd.api.model.gxd.entities;

import java.util.Date;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.bib.entities.ReferenceCitationCache;
import org.jax.mgi.mgd.api.model.mrk.entities.Marker;
import org.jax.mgi.mgd.api.model.voc.entities.Term;

import jakarta.persistence.Column;
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
@Schema(description = "Expression Cache Model Object")
@Table(name="gxd_expression")
public class ExpressionCache extends BaseEntity {

	@Id
	private int _expression_key;
	private int _stage_key;
	private String resultNote;
	@Column(columnDefinition = "int2")
	private Integer expressed;
	private String strength;
	private String age;
	//private Integer agemin;
	//private Integer agemax;
	//private Integer isrecombinase;
	//private Integer isforgxd;
	//private Integer hasimage;
	private Date creation_date;
	private Date modification_date;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_assay_key")
	private Assay assay;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_refs_key")
	private ReferenceCitationCache reference;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_assaytype_key")
	private AssayType assayType;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_genotype_key")
	private Genotype genotype;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_marker_key")
	private Marker marker;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_emapa_term_key", referencedColumnName="_term_key")
	private Term emapaTerm;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_celltype_term_key", referencedColumnName="_term_key")
	private Term cellTypeTerm;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_specimen_key")
	private Specimen specimen;
}
