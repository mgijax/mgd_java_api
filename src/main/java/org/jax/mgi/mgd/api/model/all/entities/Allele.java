package org.jax.mgi.mgd.api.model.all.entities;

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
import org.jax.mgi.mgd.api.model.img.entities.ImagePaneAssocView;
import org.jax.mgi.mgd.api.model.mgi.entities.MGIReferenceAssoc;
import org.jax.mgi.mgd.api.model.mgi.entities.MGISynonym;
import org.jax.mgi.mgd.api.model.mgi.entities.Note;
import org.jax.mgi.mgd.api.model.mgi.entities.RelationshipAlleleDriverGene;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.mrk.entities.Marker;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeStrain;
import org.jax.mgi.mgd.api.model.voc.entities.Annotation;
import org.jax.mgi.mgd.api.model.voc.entities.Term;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Allele Model Object")
@Table(name="all_allele")
public class Allele extends BaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="all_allele_generator")
	@SequenceGenerator(name="all_allele_generator", sequenceName = "all_allele_seq", allocationSize=1)
	@ApiModelProperty(value="primary key")	
	private int _allele_key;
	private String symbol;
	private String name;
	private Integer isWildType;
	private Integer isExtinct;
	private Integer isMixed;
	private Date approval_date;
	private Date creation_date;
	private Date modification_date;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_marker_key")
	private Marker marker;
		 
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_strain_key")
	private ProbeStrain strain;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_mode_key", referencedColumnName="_term_key")
	private Term inheritanceMode;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_allele_type_key", referencedColumnName="_term_key")
	private Term alleleType;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_allele_status_key", referencedColumnName="_term_key")
	private Term alleleStatus;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_transmission_key", referencedColumnName="_term_key")
	private Term transmission;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_collection_key", referencedColumnName="_term_key")
	private Term collection;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_refs_key")
	private Reference markerReference;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_markerallele_status_key", referencedColumnName="_term_key")
	private Term alleleMarkerStatus;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_approvedby_key", referencedColumnName="_user_key")
	private User approvedBy;
	
	// mgi accession ids only
	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_allele_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 11 and `_logicaldb_key` = 1 and preferred = 1")
	@OrderBy(clause="preferred desc, accID")
	private List<Accession> mgiAccessionIds;

	// other accession ids only
	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_allele_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 11 and `_logicaldb_key` != 1")
	@OrderBy(clause="preferred desc, accID")
	private List<Accession> otherAccessionIds;
	
	// reference associations
	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_allele_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 11")
	private List<MGIReferenceAssoc> refAssocs;

	// synonyms
	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_allele_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 11")
	private List<MGISynonym> synonyms;
	
	// molecular mutations
	@OneToMany()
	@JoinColumn(name="_allele_key", referencedColumnName="_allele_key", insertable=false, updatable=false)
	private List<AlleleMutation> mutations;
	
	// allele/subtype annotations(aka allele attributes)
	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_allele_key", insertable=false, updatable=false)
	@Where(clause="`_annottype_key` = 1014")
	private List<Annotation> subtypeAnnots;

	// allele/mutant cell lines
	@OneToMany()
	@JoinColumn(name="_allele_key", referencedColumnName="_allele_key", insertable=false, updatable=false)
	private List<AlleleCellLine> mutantCellLines;

	// driver gene
	@OneToMany()
	@JoinColumn(name="_allele_key", referencedColumnName="_allele_key", insertable=false, updatable=false)
	private List<RelationshipAlleleDriverGene> driverGenes;

	// image pane associations
	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_allele_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 11")
	@OrderBy(clause="isPrimary asc")
	private List<ImagePaneAssocView> imagePaneAssocs;
	
	//  1020 | General
	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_allele_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 11 and `_notetype_key` = 1020")
	private List<Note> generalNote;

	//    1021 | Molecular
	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_allele_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 11 and `_notetype_key` = 1021")
	private List<Note> molecularNote;

	//    1022 | Nomenclature
	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_allele_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 11 and `_notetype_key` = 1022")
	private List<Note> nomenNote;

	//    1032 | Inducible
	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_allele_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 11 and `_notetype_key` = 1032")
	private List<Note> inducibleNote;

	//    1036 | Associated PRO IDs
	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_allele_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 11 and `_notetype_key` = 1036")
	private List<Note> proidNote;

	//    1040 | User (Cre)
	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_allele_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 11 and `_notetype_key` = 1040")
	private List<Note> creNote;

	//    1041 | IKMC Allele Colony Name
	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_allele_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 11 and `_notetype_key` = 1041")
	private List<Note> ikmcNote;

	// DO term annotations
	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_allele_key", insertable=false, updatable=false)
	@Where(clause="`_annottype_key` = 1021")
	private List<Annotation> doAnnots;

}
