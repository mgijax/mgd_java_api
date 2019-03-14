package org.jax.mgi.mgd.api.model.mrk.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.OrderBy;
import org.hibernate.annotations.Where;
import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.acc.entities.Accession;
import org.jax.mgi.mgd.api.model.mgi.entities.MGIReferenceAssoc;
import org.jax.mgi.mgd.api.model.mgi.entities.MGISynonym;
import org.jax.mgi.mgd.api.model.mgi.entities.Note;
import org.jax.mgi.mgd.api.model.mgi.entities.Organism;
import org.jax.mgi.mgd.api.model.mgi.entities.RelationshipMarkerTSS;
import org.jax.mgi.mgd.api.model.mgi.entities.User;
import org.jax.mgi.mgd.api.model.voc.entities.Annotation;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@ApiModel(value = "Marker Entity Object")
@Table(name="mrk_marker")
public class Marker extends BaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="mrk_marker_generator")
	@SequenceGenerator(name="mrk_marker_generator", sequenceName = "mrk_marker_seq", allocationSize=1)
	@ApiModelProperty(value="primary key")
	private int _marker_key;
	
	private String symbol;
	private String name;
	private String chromosome;
	private String cytogeneticOffset;
	private Double cmOffset;
	private Date creation_date;
	private Date modification_date;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_organism_key")
	private Organism organism;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="_marker_status_key")
	private MarkerStatus markerStatus;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_marker_type_key")
	private MarkerType markerType;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_createdby_key", referencedColumnName="_user_key")
	private User createdBy;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_modifiedby_key", referencedColumnName="_user_key")
	private User modifiedBy;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_marker_key", referencedColumnName="_marker_key")
	private MarkerLocationCache locationCache;
	
	// insertable/updatable=false will ignore dereferencing these objects
	// postgres trigger will delete the child object if the parent is deleted
	
	// Editor/Coordinator
	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_marker_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 2 and `_notetype_key` = 1004")
	private List<Note> editorNote;
	
	// Sequence
	@OneToMany
	@JoinColumn(name="_object_key", referencedColumnName="_marker_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 2 and `_notetype_key` = 1009")
	private List<Note> sequenceNote;
	
	//Marker Revision
	@OneToMany
	@JoinColumn(name="_object_key", referencedColumnName="_marker_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 2 and `_notetype_key` = 1030")
	private List<Note> revisionNote;
	
	// Strain-Specific Marker
	@OneToMany
	@JoinColumn(name="_object_key", referencedColumnName="_marker_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 2 and `_notetype_key` = 1035")
	private List<Note> strainNote;
	
	// Location
	@OneToMany
	@JoinColumn(name="_object_key", referencedColumnName="_marker_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 2 and `_notetype_key` = 1049")
	private List<Note> locationNote;
	
	// mgi accession ids only
	@OneToMany
	@JoinColumn(name="_object_key", referencedColumnName="_marker_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 2 and `_logicaldb_key` = 1")
	@OrderBy(clause="preferred desc, accID")
	private List<Accession> mgiAccessionIds;
	
	// editable only accession ids
	@OneToMany
	@JoinColumn(name="_object_key", referencedColumnName="_marker_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 2 and `_logicaldb_key` in (8,9)")
	@OrderBy(clause ="accid")
	private List<Accession> editAccessionIds;
	
	// non-editable accession ids
	@OneToMany
	@JoinColumn(name="_object_key", referencedColumnName="_marker_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 2 and `_logicaldb_key` not in (1,8,9,117,118)")
	@OrderBy(clause ="accid")
	private List<Accession> nonEditAccessionIds;
	
	@OneToMany
	@JoinColumn(name="_marker_key", insertable=false, updatable=false)
	@OrderBy(clause="sequenceNum")
	private List<MarkerHistory> history;
	
	@OneToMany
	@JoinColumn(name="_object_key", referencedColumnName="_marker_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 2")
	@OrderBy(clause ="_synonymtype_key, synonym")
	private List<MGISynonym> synonyms;
	
	@OneToMany
	@JoinColumn(name="_object_key", referencedColumnName="_marker_key", insertable=false, updatable=false)
	@Where(clause="`_mgitype_key` = 2")
	@OrderBy(clause ="_refassoctype_key")
	private List<MGIReferenceAssoc> refAssocs;

	@OneToMany
	@JoinColumn(name="_object_key_1", referencedColumnName="_marker_key", insertable=false, updatable=false)
	private List<RelationshipMarkerTSS> tssToGene;
	
	@OneToMany
	@JoinColumn(name="_object_key_2", referencedColumnName="_marker_key", insertable=false, updatable=false)
	private List<RelationshipMarkerTSS> geneToTss;

	@OneToMany
	@JoinTable(name = "mrk_alias",	joinColumns = @JoinColumn(name="_alias_key", referencedColumnName="_marker_key", insertable=false, updatable=false),
		inverseJoinColumns = @JoinColumn(name = "_marker_key", referencedColumnName="_marker_key", insertable=false, updatable=false)
	)
	private List<Marker> aliases;

	@OneToMany
	@JoinColumn(name="_object_key", referencedColumnName="_marker_key", insertable=false, updatable=false)
	@Where(clause="`_annottype_key` = 1011")	
	private List<Annotation> featureTypes;
	
}
