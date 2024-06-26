package org.jax.mgi.mgd.api.model.gxd.entities;

import java.util.Date;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.hibernate.annotations.OrderBy;
import org.hibernate.annotations.Where;
import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.acc.entities.Accession;
import org.jax.mgi.mgd.api.model.img.entities.ImagePaneAssocView;
import org.jax.mgi.mgd.api.model.mgi.entities.Note;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeStrain;
import org.jax.mgi.mgd.api.model.voc.entities.Annotation;
import org.jax.mgi.mgd.api.model.voc.entities.AnnotationHeader;
import org.jax.mgi.mgd.api.model.voc.entities.Term;

import jakarta.persistence.Column;
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
@Schema(description = "Genotype Model Object")
@Table(name="gxd_genotype")
public class Genotype extends BaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="gxd_genotype_generator")
	@SequenceGenerator(name="gxd_genotype_generator", sequenceName = "gxd_genotype_seq", allocationSize=1)
	@Schema(name="primary key")	
	private int _genotype_key;
	
	@Column(columnDefinition = "int2")
	private Integer isConditional;
	
	// obsolte/remove : all are null
	// not included in domain
	private String note;
	
	private Date creation_date;
	private Date modification_date;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_strain_key")
	private ProbeStrain strain;
	
	// _vocab_key = 60, "Genotype Exists As" vocabulary
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_existsas_key", referencedColumnName="_term_key")
	private Term existsAs;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;
	
	// Combination Type 1
	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_genotype_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 12 and `_notetype_key` = 1016")
	private List<Note> alleleDetailNote;
	
	// General
	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_genotype_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 12 and `_notetype_key` = 1027")
	private List<Note> generalNote;
	
	// Private Curatorial
	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_genotype_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 12 and `_notetype_key` = 1028")
	private List<Note> privateCuratorialNote;
	
	// mgi accession ids only
	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_genotype_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 12 and `_logicaldb_key` = 1")
	@OrderBy(clause="preferred desc, accID")
	private List<Accession> mgiAccessionIds;
	
	// resource identification initiative accession ids only
	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_genotype_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 12 and `_logicaldb_key` = 179")
	@OrderBy(clause="preferred desc, accID")
	private List<Accession> resourceIdentifierAccessionIds;
	
	// allele pairs
	@OneToMany()
	@JoinColumn(name="_genotype_key", referencedColumnName="_genotype_key", insertable=false, updatable=false)
	private List<AllelePair> allelePairs;
	
	// image pane associations
	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_genotype_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 12")
	@OrderBy(clause="isPrimary asc")
	private List<ImagePaneAssocView> imagePaneAssocs;
    
	// mp term annotations
	@OneToMany()
    @JoinColumn(name="_object_key", referencedColumnName="_genotype_key", insertable=false, updatable=false)
    @Where(clause="`_annottype_key` = 1002")
    private List<Annotation> mpAnnots;
	
	// MP Header by Genotype
	@OneToMany()
    @JoinColumn(name="_object_key", referencedColumnName="_genotype_key", insertable=false, updatable=false)
    @Where(clause="`_annottype_key` = 1002")
    private List<AnnotationHeader> mpHeaders;
		
	// DO term annotations
	@OneToMany()
    @JoinColumn(name="_object_key", referencedColumnName="_genotype_key", insertable=false, updatable=false)
    @Where(clause="`_annottype_key` = 1020")
    private List<Annotation> doAnnots;
	
}
