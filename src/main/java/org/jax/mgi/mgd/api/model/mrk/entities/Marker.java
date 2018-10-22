package org.jax.mgi.mgd.api.model.mrk.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.OrderBy;
import org.hibernate.annotations.Where;
import org.jax.mgi.mgd.api.model.BaseEntity;
import org.jax.mgi.mgd.api.model.acc.entities.Accession;
import org.jax.mgi.mgd.api.model.mgi.entities.Note;
import org.jax.mgi.mgd.api.model.mgi.entities.Organism;
import org.jax.mgi.mgd.api.model.mgi.entities.User;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name="mrk_marker")
public class Marker extends BaseEntity {

	@Id
	@ApiModelProperty(value="primary key")
	private Integer _marker_key;
	private String symbol;
	private String name;
	private String chromosome;
	private String cytogeneticOffset;
	private String cmOffset;
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
	
	// Editor/Coordinator
	@OneToMany()
	@JoinColumn(name="_object_key", referencedColumnName="_marker_key")
	@Where(clause="`_mgitype_key` = 2 and `_notetype_key` = 1004")
	private Set<Note> editorNote;
	
	// Sequence
	@OneToMany
	@JoinColumn(name="_object_key", referencedColumnName="_marker_key")
	@Where(clause="`_mgitype_key` = 2 and `_notetype_key` = 1009")
	private Set<Note> sequenceNote;
	
	//Marker Revision
	@OneToMany
	@JoinColumn(name="_object_key", referencedColumnName="_marker_key")
	@Where(clause="`_mgitype_key` = 2 and `_notetype_key` = 1030")
	private Set<Note> revisionNote;
	
	// Strain-Specific Marker
	@OneToMany
	@JoinColumn(name="_object_key", referencedColumnName="_marker_key")
	@Where(clause="`_mgitype_key` = 2 and `_notetype_key` = 1035")
	private Set<Note> strainNote;
	
	// Location
	@OneToMany
	@JoinColumn(name="_object_key", referencedColumnName="_marker_key")
	@Where(clause="`_mgitype_key` = 2 and `_notetype_key` = 1049")
	private Set<Note> locationNote;
	
	// marker note aka marker detail clip (see Allele module)
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="_marker_key")
	private MarkerNote markerNote;
	
	@OneToMany
	@JoinColumn(name="_object_key", referencedColumnName="_marker_key")
	@Where(clause="`_mgitype_key` = 2 and `_logicaldb_key` = 1")
	@OrderBy(clause = "preferred, accID")
	private Set<Accession> mgiAccessionIds;
	
	//@OneToMany
	//@JoinColumn(name="_marker_key")
	//private ArrayList<MarkerHistory> history;
	
	//@OneToOne(fetch=FetchType.LAZY)
	//@JoinColumn(name="_marker_key")
	//private MarkerLocationCache markerLocation;

	//@OneToMany
	//@JoinColumn(name="_object_key", referencedColumnName="_marker_key")
	//@Where(clause="`_mgitype_key` = 2")
	//@OrderColumn(name="accID")
	//private Set<Accession> allAccessionIds;

	//@OneToMany
	//@JoinColumn(name="_marker_key")
	//private Set<Allele> alleles = new HashSet<Allele>();
	
	//@OneToMany
	//@JoinColumn(name="_marker_key")
	//private Set<MarkerStrain> markerStrain;
	
	//@OneToMany
	//@JoinColumn(name="_marker_key")
	//private Set<ProbeMarker> probeMarkers;

	//@OneToMany
	//@JoinColumn(name="_marker_key")
	//private Set<ProbeStrainMarker> probeStrainMarkers;
	
	//@OneToMany
	//@JoinColumn(name="_marker_key")
	//private Set<Assay> assays;
	
	//@OneToMany
	//@JoinColumn(name="_marker_key")
	//private Set<ExpressionCache> assayResults;
	
	//@OneToMany
	//@JoinColumn(name="_marker_key")
	//private Set<Index> indexes;
	
	//@OneToMany
	//@JoinColumn(name="_marker_key")
	//private Set<ExptMarker> exptMarkers;
	
	//@OneToMany
	//@JoinColumn(name="_marker_key")
	//private Set<SequenceMarkerCache> sequenceMarkers;
	
	//@OneToMany
	//@JoinColumn(name="_marker_key")
	//@Where(clause="`_logicaldb_key` in (59, 60)")
	//private Set<SequenceMarkerCache> biotypes;
	
	//@OneToMany
	//@JoinColumn(name="_marker_key")
	//private Set<MarkerReferenceCache> referenceMarkers;
	
	//@OneToMany
	//@JoinColumn(name="_marker_key")
	//@Where(clause="`qualifier` = 'D' ")
	//private Set<MarkerMCVCache> mcvTerms;
	
	//@OneToMany
	//@JoinColumn(name="_object_key", referencedColumnName="_marker_key")
	//@Where(clause="`_mgitype_key` = 2")
	//private Set<CoordinateFeature> features;

	//@OneToMany
	//@JoinColumn(name="_object_key", referencedColumnName="_marker_key")
	//@Where(clause="`_mgitype_key` = 2 and `_notetype_key` = 1049")
	//private Set<Note> locationNotes;
	
	//@OneToMany
	//@JoinColumn(name="_object_key", referencedColumnName="_marker_key")
	//@Where(clause="`_mgitype_key` = 2")
	//private Set<MGISynonym> synonyms;
	
	//@OneToMany
	//@JoinColumn(name="_object_key_1", referencedColumnName="_marker_key")
	//@Where(clause="`_category_key` = 1008")
	//private Set<Relationship> tssToGeneRelationships;
	
	//@OneToMany
	//@JoinColumn(name="_object_key_2", referencedColumnName="_marker_key")
	//@Where(clause="`_category_key` = 1008")
	//private Set<Relationship> geneToTssRelationships;
	
	//@ManyToMany
	//@JoinTable(name = "mrk_alias",
	//	joinColumns = @JoinColumn(name = "_alias_key", referencedColumnName="_marker_key"),
	//	inverseJoinColumns = @JoinColumn(name = "_marker_key", referencedColumnName="_marker_key")
	//)
	//private Set<Marker> aliases;

	//@ManyToMany
	//@JoinTable(name = "mrk_current",
	//	joinColumns = @JoinColumn(name = "_current_key", referencedColumnName="_marker_key"),
	//	inverseJoinColumns = @JoinColumn(name = "_marker_key", referencedColumnName="_marker_key")
	//)
	//private Set<Marker> currentMarkers;

	//@ManyToMany
	//@JoinTable(name = "gxd_antibodymarker",
	//	joinColumns = @JoinColumn(name = "_marker_key"),
	//	inverseJoinColumns = @JoinColumn(name = "_antibody_key")
	//)
	//private Set<Antibody> antibodies;

	//public Accession getMgiAccessionId() {
	//	for(Accession a: allAccessionIds) {
	//		if(a.get_mgitype_key() == 2 
	//				&& a.get_logicaldb_key() == 1 
	//				&& a.getPreferred() == 1) {
	//			return a;
	//		}
	//	}
	//	return new Accession();
	//}

	//@Transient
	//public Set<Accession> getSecondaryMgiAccessionIds() {
	//	HashSet<Accession> set = new HashSet<Accession>();
	//	for(Accession a: allAccessionIds) {
	//		if(a.get_mgitype_key() == 2 
	//				&& a.get_logicaldb_key() == 1 
	//				&& a.getPreferred() == 0
	//				&& a.getPrefixPart().equals("MGI:")) {
	//			set.add(a);
	//		}
	//	}
	//	return set;
	//}

	//@Transient
	//public Set<Accession> getAccessionIdsByLogicalDb(LogicalDB db) {
	//	return getAccessionIdsByLogicalDb(db.get_logicaldb_key());
	//}
	
	//@Transient
	//public Set<Accession> getAccessionIdsByLogicalDb(Integer db_key) {
	//	HashSet<Accession> set = new HashSet<Accession>();
	//	for(Accession a: allAccessionIds) {
	//		if(a.get_logicaldb_key() == db_key) {
	//			set.add(a);
	//		}
	//	}
	//	return set;
	//}
	
}
