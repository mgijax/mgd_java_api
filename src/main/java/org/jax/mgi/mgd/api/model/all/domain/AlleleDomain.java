package org.jax.mgi.mgd.api.model.all.domain;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.acc.domain.AccessionDomain;
import org.jax.mgi.mgd.api.model.img.domain.ImagePaneAssocViewDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.MGIReferenceAssocDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.MGISynonymDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.NoteDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.RelationshipAlleleDriverGeneDomain;
import org.jax.mgi.mgd.api.model.mrk.domain.MarkerNoteDomain;
import org.jax.mgi.mgd.api.model.voc.domain.AnnotationDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AlleleDomain extends BaseDomain {

	private String alleleKey;
	private String symbol;
	private String name;
	private String isWildType;
	private String isExtinct;
	private String isMixed;
	private String inheritanceModeKey;
	private String inheritanceMode;
	private String alleleTypeKey;
	private String alleleType;
	private String alleleStatusKey;
	private String alleleStatus;	
	private String markerAlleleStatusKey;
	private String markerAlleleStatus;
	private String transmissionKey;
	private String transmission;
	private String collectionKey;
	private String collection;
	private String strainOfOriginKey;
	private String strainOfOrigin;
	private String accID;

	private String markerKey;
	private String markerSymbol;
	private String markerStatusKey;
	private String markerStatus;
	private String chromosome;
	private String strand;
	private String refsKey;
	private String jnumid;
	private Integer jnum;
	private String short_citation;
	
	private String createdByKey;
	private String createdBy;
	private String modifiedByKey;
	private String modifiedBy;
	private String approvedByKey;
	private String approvedBy;
	private String creation_date;
	private String modification_date;
	private String approval_date;
	
	// molecular reference needed for driverGenes
	private String molRefKey;
	
	// mixed reference 
	private String mixedRefKey;
	
	private List<AccessionDomain> otherAccIDs;
	private List<MGIReferenceAssocDomain> refAssocs;

	private List<AlleleCellLineDomain> mutantCellLineAssocs;

	private List<MGISynonymDomain> synonyms;
	private List<AlleleMutationDomain> mutations;
	private List<AnnotationDomain> subtypeAnnots;
	private List<RelationshipAlleleDriverGeneDomain> driverGenes;
	private List<ImagePaneAssocViewDomain> imagePaneAssocs;
	
	private MarkerNoteDomain detailClip;
	private NoteDomain generalNote;
	private NoteDomain molecularNote;
	private NoteDomain nomenNote;
	private NoteDomain inducibleNote;
	private NoteDomain proidNote;
	private NoteDomain creNote;
	private NoteDomain ikmcNote;
	
}
