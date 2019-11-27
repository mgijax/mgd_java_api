package org.jax.mgi.mgd.api.model.mrk.translator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.acc.domain.AccessionDomain;
import org.jax.mgi.mgd.api.model.acc.translator.AccessionTranslator;
import org.jax.mgi.mgd.api.model.mgi.domain.MGISynonymDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.NoteDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.RelationshipMarkerTSSDomain;
import org.jax.mgi.mgd.api.model.mgi.translator.MGISynonymTranslator;
import org.jax.mgi.mgd.api.model.mgi.translator.NoteTranslator;
import org.jax.mgi.mgd.api.model.mgi.translator.RelationshipMarkerTSSTranslator;
import org.jax.mgi.mgd.api.model.mrk.domain.MarkerDomain;
import org.jax.mgi.mgd.api.model.mrk.domain.MarkerHistoryDomain;
import org.jax.mgi.mgd.api.model.mrk.domain.MarkerNoteDomain;
import org.jax.mgi.mgd.api.model.mrk.domain.SlimMarkerDomain;
import org.jax.mgi.mgd.api.model.mrk.entities.Marker;
import org.jax.mgi.mgd.api.model.voc.domain.MarkerFeatureTypeDomain;
import org.jax.mgi.mgd.api.model.voc.translator.MarkerFeatureTypeTranslator;
import org.jboss.logging.Logger;

public class MarkerTranslator extends BaseEntityDomainTranslator<Marker, MarkerDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	private NoteTranslator noteTranslator = new NoteTranslator();
	private AccessionTranslator accessionTranslator = new AccessionTranslator();
	private MarkerHistoryTranslator historyTranslator = new MarkerHistoryTranslator();
	private MGISynonymTranslator synonymTranslator = new MGISynonymTranslator();
	private MarkerFeatureTypeTranslator featureTypeTranslator = new MarkerFeatureTypeTranslator();
	private SlimMarkerTranslator slimMarkerTranslator = new SlimMarkerTranslator();
	private RelationshipMarkerTSSTranslator markerTSSTranslator = new RelationshipMarkerTSSTranslator();
	
	@Override
	protected MarkerDomain entityToDomain(Marker entity) {
		
		MarkerDomain domain = new MarkerDomain();

		// do not use 'processStatus' because this is a master domain
		// and only 1 master domain record is processed by the create/update endpoint
		
		domain.setMarkerKey(String.valueOf(entity.get_marker_key()));
		domain.setSymbol(entity.getSymbol());
		domain.setName(entity.getName());
		domain.setChromosome(entity.getChromosome());
		
		if (entity.getCytogeneticOffset() != null) {
			domain.setCytogeneticOffset(entity.getCytogeneticOffset());
		}
		
		if (entity.getCmOffset() != null) {
			domain.setCmOffset(entity.getCmOffset().toString());
		}
		
		domain.setOrganismKey(String.valueOf(entity.getOrganism().get_organism_key()));
		domain.setOrganism(entity.getOrganism().getCommonname());
		domain.setMarkerStatusKey(entity.getMarkerStatus().get_marker_status_key().toString());
		domain.setMarkerStatus(entity.getMarkerStatus().getStatus());
		domain.setMarkerTypeKey(entity.getMarkerType().get_marker_type_key().toString());
		domain.setMarkerType(entity.getMarkerType().getName());
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
		
		// at most one editorNote
		if (entity.getEditorNote() != null && !entity.getEditorNote().isEmpty()) {
			Iterable<NoteDomain> editorNote = noteTranslator.translateEntities(entity.getEditorNote());
			domain.setEditorNote(editorNote.iterator().next());
		}

		// at most one sequenceNote
		if (entity.getSequenceNote() != null && !entity.getSequenceNote().isEmpty()) {
			Iterable<NoteDomain> sequenceNote = noteTranslator.translateEntities(entity.getSequenceNote());
			domain.setSequenceNote(sequenceNote.iterator().next());
		}
		
		// at most one revisionNote
		if (entity.getRevisionNote() != null && !entity.getRevisionNote().isEmpty()) {
			Iterable<NoteDomain> revisionNote = noteTranslator.translateEntities(entity.getRevisionNote());
			domain.setRevisionNote(revisionNote.iterator().next());
		}
		
		// at most one strainNote
		if (entity.getStrainNote() != null && !entity.getStrainNote().isEmpty()) {
			Iterable<NoteDomain> strainNote = noteTranslator.translateEntities(entity.getStrainNote());
			domain.setStrainNote(strainNote.iterator().next());
		}
		
		// at most one locationNote
		if (entity.getLocationNote() != null && !entity.getLocationNote().isEmpty()) {
			Iterable<NoteDomain> locationNote = noteTranslator.translateEntities(entity.getLocationNote());
			domain.setLocationNote(locationNote.iterator().next());
		}
		
		// at most one detailClipNote
		if (entity.getDetailClipNote() != null && !entity.getDetailClipNote().isEmpty()) {
			MarkerNoteTranslator markerNoteTranslator = new MarkerNoteTranslator();
			Iterable<MarkerNoteDomain> clipNote = markerNoteTranslator.translateEntities(entity.getDetailClipNote());
			domain.setDetailClipNote(clipNote.iterator().next());
		}
				
		// mgi accession ids only
		if (entity.getMgiAccessionIds() != null && !entity.getMgiAccessionIds().isEmpty()) {
			Iterable<AccessionDomain> acc = accessionTranslator.translateEntities(entity.getMgiAccessionIds());
			domain.setMgiAccessionIds(IteratorUtils.toList(acc.iterator()));
		}
		
		// one-to-many marker history
		if (entity.getHistory() != null && !entity.getHistory().isEmpty()) {
			Iterable<MarkerHistoryDomain> i = historyTranslator.translateEntities(entity.getHistory());
			domain.setHistory(IteratorUtils.toList(i.iterator()));
			//cannot use this for keys as they are stored as strings
			//domain.getHistory().sort(Comparator.comparing(MarkerHistoryDomain::getSequenceNum));
		}
		
		// one-to-many marker synonyms
		if (entity.getSynonyms() != null && !entity.getSynonyms().isEmpty()) {
			Iterable<MGISynonymDomain> i = synonymTranslator.translateEntities(entity.getSynonyms());
			domain.setSynonyms(IteratorUtils.toList(i.iterator()));
			domain.getSynonyms().sort(Comparator.comparing(MGISynonymDomain::getSynonymTypeKey).thenComparing(MGISynonymDomain::getSynonym, String.CASE_INSENSITIVE_ORDER));
		}

		// one-to-many marker feature types
		// featureTypes can also be set via voc/service/Annotation.service/markerFeatureTypes
		if (entity.getFeatureTypes() != null && !entity.getFeatureTypes().isEmpty()) {
			Iterable<MarkerFeatureTypeDomain> i = featureTypeTranslator.translateEntities(entity.getFeatureTypes());
			domain.setFeatureTypes(IteratorUtils.toList(i.iterator()));
			domain.getFeatureTypes().sort(Comparator.comparing(MarkerFeatureTypeDomain::getTerm));
		}
		
		// one-to-many tss-to-gene relationships
		List<SlimMarkerDomain> markers = new ArrayList<SlimMarkerDomain>();
		if (entity.getTssToGene() != null && !entity.getTssToGene().isEmpty()) {
    		Iterable<RelationshipMarkerTSSDomain> relationships = markerTSSTranslator.translateEntities(entity.getTssToGene());
			for (RelationshipMarkerTSSDomain i : relationships) {
				if (domain.getMarkerKey().equals(i.getObjectKey1())) {
					SlimMarkerDomain markerDomain = new SlimMarkerDomain();				
					markerDomain.setMarkerKey(i.getObjectKey2());
					markerDomain.setSymbol(i.getSymbol2());
					markers.add(markerDomain);
				}
    		}
    		domain.setTssToGene(markers);
			domain.getTssToGene().sort(Comparator.comparing(SlimMarkerDomain::getSymbol, String.CASE_INSENSITIVE_ORDER));
		}
		if (entity.getGeneToTss() != null && !entity.getGeneToTss().isEmpty()) {
    		Iterable<RelationshipMarkerTSSDomain> relationships = markerTSSTranslator.translateEntities(entity.getGeneToTss());
			for (RelationshipMarkerTSSDomain i : relationships) {
				if (domain.getMarkerKey().equals(i.getObjectKey2())) {
					SlimMarkerDomain markerDomain = new SlimMarkerDomain();				
					markerDomain.setMarkerKey(i.getObjectKey1());
					markerDomain.setSymbol(i.getSymbol1());
					markers.add(markerDomain);
				}
    		}
    		domain.setTssToGene(markers);
			domain.getTssToGene().sort(Comparator.comparing(SlimMarkerDomain::getSymbol, String.CASE_INSENSITIVE_ORDER));
		}
		   	
		// one-to-many marker aliases
		if (entity.getAliases() != null && !entity.getAliases().isEmpty()) {
			Iterable<SlimMarkerDomain> i = slimMarkerTranslator.translateEntities(entity.getAliases());
			domain.setAliases(IteratorUtils.toList(i.iterator()));
			domain.getAliases().sort(Comparator.comparing(SlimMarkerDomain::getSymbol, String.CASE_INSENSITIVE_ORDER));
		}

		// accession ids editable
		if (entity.getEditAccessionIds() != null && !entity.getEditAccessionIds().isEmpty()) {
			Iterable<AccessionDomain> acc = accessionTranslator.translateEntities(entity.getEditAccessionIds());
			domain.setEditAccessionIds(IteratorUtils.toList(acc.iterator()));
			domain.getEditAccessionIds().sort(Comparator.comparing(AccessionDomain::getLogicaldb).thenComparing(AccessionDomain::getAccID));
		}
		
		// accession ids non-editable 
		if (entity.getNonEditAccessionIds() != null && !entity.getNonEditAccessionIds().isEmpty()) {
			Iterable<AccessionDomain> acc = accessionTranslator.translateEntities(entity.getNonEditAccessionIds());
			domain.setNonEditAccessionIds(IteratorUtils.toList(acc.iterator()));
			domain.getNonEditAccessionIds().sort(Comparator.comparing(AccessionDomain::getLogicaldb).thenComparing(AccessionDomain::getAccID));
		}
		
		// these domains are only set by individual object endpoints
		// that is, see acc/service/AccessionService:getMarkerEditAccessionIds
							
		// reference associations
		//if (entity.getRefAssocs() != null && !entity.getRefAssocs().isEmpty()) {
		//	MGIReferenceAssocTranslator refAssocTranslator = new MGIReferenceAssocTranslator();
		//	Iterable<MGIReferenceAssocDomain> i = refAssocTranslator.translateEntities(entity.getRefAssocs());
		//	domain.setRefAssocs(IteratorUtils.toList(i.iterator()));
		//	domain.setRefAssocs().sort(Comparator.comparing(MGIReferenceAssocDomain::getRefAssocTypeKey).thenComparing(MGIReferenceAssocDomain.getJnum));
		//}						
		
		return domain;
	}

}
