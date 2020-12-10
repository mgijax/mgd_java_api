package org.jax.mgi.mgd.api.model.prb.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
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
import org.jax.mgi.mgd.api.model.mgi.entities.MGIReferenceAssoc;
import org.jax.mgi.mgd.api.model.mgi.entities.MGISynonym	;
import org.jax.mgi.mgd.api.model.mgi.entities.Note;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.entities.Annotation;
import org.jax.mgi.mgd.api.model.voc.entities.Term;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Strain Model Object")
@Table(name="prb_strain")
public class ProbeStrain extends BaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="prb_strain_generator")
	@SequenceGenerator(name="prb_strain_generator", sequenceName = "prb_strain_seq", allocationSize=1)
	@ApiModelProperty(value="primary key")		
	private int _strain_key;
	
	private String strain;
	private int standard;
	@Column(name="private") // just "private" is a Java reserved word
	private int isPrivate;
	private int geneticBackground;
	private Date creation_date;
	private Date modification_date;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_species_key", referencedColumnName="_term_key")
	private Term species;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_straintype_key", referencedColumnName="_term_key")
	private Term strainType;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;

	// mgi accession ids only
	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_strain_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 10 and `_logicaldb_key` = 1")
	@OrderBy(clause="preferred desc, accID")
	private List<Accession> mgiAccessionIds;

	// other accession ids only
	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_strain_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 10 and `_logicaldb_key` != 1")
	@OrderBy(clause="preferred desc, accID")
	private List<Accession> otherAccessionIds;
	
	// Strain Attributes
	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_strain_key", insertable=false, updatable=false)
	@Where(clause="`_annottype_key` = 1009")
	private List<Annotation> attributes;	

	// Needs Review
	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_strain_key", insertable=false, updatable=false)
	@Where(clause="`_annottype_key` = 1008")
	private List<Annotation> needsReview;	
	
	// Markers
	@OneToMany()
	@JoinColumn(name="_strain_key", insertable=false, updatable=false)
	private List<ProbeStrainMarker> markers;
	
	// Genotypes
	@OneToMany()
	@JoinColumn(name="_strain_key", insertable=false, updatable=false)
	private List<ProbeStrainGenotype> genotypes;
	
	//Synonyms
	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_strain_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 10")
	private List<MGISynonym> synonyms;

	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_strain_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 10")
	@OrderBy(clause="_refassoctype_key")
	private List<MGIReferenceAssoc> refAssocs;
	
	// Strain Origin
	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_strain_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 10 and `_notetype_key` = 1011")
	private List<Note> strainOriginNote;

	// IMPC Colony ID
	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_strain_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 10 and `_notetype_key` = 1012")
	private List<Note> impcColonyNote;
	
	// Nomenclature
	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_strain_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 10 and `_notetype_key` = 1013")
	private List<Note> nomenNote;

	// Mutant Cell Line of Origin
	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_strain_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 10 and `_notetype_key` = 1038")
	private List<Note> mutantCellLineNote;

}
