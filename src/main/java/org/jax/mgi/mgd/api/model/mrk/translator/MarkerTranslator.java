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
import org.jax.mgi.mgd.api.model.mgi.domain.RelationshipMarkerQTLCandidateDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.RelationshipMarkerQTLInteractionDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.RelationshipMarkerTSSDomain;
import org.jax.mgi.mgd.api.model.mgi.translator.MGISynonymTranslator;
import org.jax.mgi.mgd.api.model.mgi.translator.NoteTranslator;
import org.jax.mgi.mgd.api.model.mgi.translator.RelationshipMarkerQTLCandidateTranslator;
import org.jax.mgi.mgd.api.model.mgi.translator.RelationshipMarkerQTLInteractionTranslator;
import org.jax.mgi.mgd.api.model.mgi.translator.RelationshipMarkerTSSTranslator;
import org.jax.mgi.mgd.api.model.mrk.domain.MarkerDomain;
import org.jax.mgi.mgd.api.model.mrk.domain.MarkerHistoryDomain;
import org.jax.mgi.mgd.api.model.mrk.domain.MarkerMCVDirectDomain;
import org.jax.mgi.mgd.api.model.mrk.domain.MarkerNoteDomain;
import org.jax.mgi.mgd.api.model.mrk.domain.SlimMarkerDomain;
import org.jax.mgi.mgd.api.model.mrk.entities.Marker;
import org.jax.mgi.mgd.api.model.seq.domain.SeqMarkerBiotypeDomain;
import org.jax.mgi.mgd.api.model.seq.translator.SeqMarkerBiotypeTranslator;
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
	private MarkerMCVDirectTranslator featureTypeDirectTranslator = new MarkerMCVDirectTranslator();
	private RelationshipMarkerTSSTranslator markerTSSTranslator = new RelationshipMarkerTSSTranslator();
	private RelationshipMarkerQTLCandidateTranslator markerQTLCandidateTranslator = new RelationshipMarkerQTLCandidateTranslator();
	private RelationshipMarkerQTLInteractionTranslator markerQTLInteractionTranslator = new RelationshipMarkerQTLInteractionTranslator();
	private SeqMarkerBiotypeTranslator biotypeTranslator = new SeqMarkerBiotypeTranslator();

	@Override
	protected MarkerDomain entityToDomain(Marker entity) {
		
		MarkerDomain domain = new MarkerDomain();

		// do not use 'processStatus' because this is a master domain
		// and only 1 master domain record is processed by the create/update endpoint
		
		domain.setMarkerKey(String.valueOf(entity.get_marker_key()));
		domain.setSymbol(entity.getSymbol());
		domain.setName(entity.getName());
		domain.setChromosome(entity.getChromosome());
		
		if (entity.getLocationCache() != null) {
			if (entity.getLocationCache().getGenomicChromosome() != null) {
				domain.setGenomicChromosome(entity.getLocationCache().getGenomicChromosome());
			}
		}
		
		if (entity.getCytogeneticOffset() != null) {
			domain.setCytogeneticOffset(entity.getCytogeneticOffset());
		}
		
		if (entity.getCmOffset() != null) {
			domain.setCmOffset(entity.getCmOffset().toString());
		}
		
		domain.setOrganismKey(String.valueOf(entity.getOrganism().get_organism_key()));
		domain.setOrganism(entity.getOrganism().getCommonname());
		domain.setOrganismLatin(entity.getOrganism().getLatinname());
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
		
		// at most one location cache
		if (entity.getLocationCache() != null ) {
                        Double sc = entity.getLocationCache().getStartCoordinate();
                        Double ec = entity.getLocationCache().getEndCoordinate();
			domain.setStartCoordinate(sc == null ? "null" : String.valueOf(sc.intValue()));
			domain.setEndCoordinate  (ec == null ? "null" : String.valueOf(ec.intValue()));
			domain.setStrand(String.valueOf(entity.getLocationCache().getStrand()));
			domain.setMapUnits(entity.getLocationCache().getMapUnits());
			domain.setProvider(entity.getLocationCache().getProvider());
			domain.setVersion(entity.getLocationCache().getVersion());
		}
		
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
		
		// one-to-many marker feature types direct/should be only one
		if (entity.getFeatureTypesDirect() != null && !entity.getFeatureTypesDirect().isEmpty()) {
			Iterable<MarkerMCVDirectDomain> i = featureTypeDirectTranslator.translateEntities(entity.getFeatureTypesDirect());
			domain.setFeatureTypesDirect(IteratorUtils.toList(i.iterator()));
		}
		
		// one-to-many tss-to-gene relationships
		List<SlimMarkerDomain> markertss = new ArrayList<SlimMarkerDomain>();
		if (entity.getTssToGene() != null && !entity.getTssToGene().isEmpty()) {
    		Iterable<RelationshipMarkerTSSDomain> relationships = markerTSSTranslator.translateEntities(entity.getTssToGene());
			for (RelationshipMarkerTSSDomain i : relationships) {
				if (domain.getMarkerKey().equals(i.getObjectKey1())) {
					SlimMarkerDomain markerDomain = new SlimMarkerDomain();				
					markerDomain.setMarkerKey(i.getObjectKey2());
					markerDomain.setSymbol(i.getSymbol2());
					markertss.add(markerDomain);
				}
    		}
    		domain.setTssToGene(markertss);
			domain.getTssToGene().sort(Comparator.comparing(SlimMarkerDomain::getSymbol, String.CASE_INSENSITIVE_ORDER));
		}
		if (entity.getGeneToTss() != null && !entity.getGeneToTss().isEmpty()) {
    		Iterable<RelationshipMarkerTSSDomain> relationships = markerTSSTranslator.translateEntities(entity.getGeneToTss());
			for (RelationshipMarkerTSSDomain i : relationships) {
				if (domain.getMarkerKey().equals(i.getObjectKey2())) {
					SlimMarkerDomain markerDomain = new SlimMarkerDomain();				
					markerDomain.setMarkerKey(i.getObjectKey1());
					markerDomain.setSymbol(i.getSymbol1());
					markertss.add(markerDomain);
				}
    		}
    		domain.setTssToGene(markertss);
			domain.getTssToGene().sort(Comparator.comparing(SlimMarkerDomain::getSymbol, String.CASE_INSENSITIVE_ORDER));
		}
		   	
		// one-to-many qtl-candidate-to-gene relationships
		List<SlimMarkerDomain> markerqtlc = new ArrayList<SlimMarkerDomain>();
		if (entity.getQtlCandidateToGene() != null && !entity.getQtlCandidateToGene().isEmpty()) {
    		Iterable<RelationshipMarkerQTLCandidateDomain> relationships = markerQTLCandidateTranslator.translateEntities(entity.getQtlCandidateToGene());
			for (RelationshipMarkerQTLCandidateDomain i : relationships) {
				if (domain.getMarkerKey().equals(i.getObjectKey1())) {
					SlimMarkerDomain markerDomain = new SlimMarkerDomain();				
					markerDomain.setMarkerKey(i.getObjectKey2());
					markerDomain.setSymbol(i.getSymbol2() + " (" + i.getJnumid() + ")");
					markerqtlc.add(markerDomain);
				}
    		}
    		domain.setQtlCandidateToGene(markerqtlc);
			domain.getQtlCandidateToGene().sort(Comparator.comparing(SlimMarkerDomain::getSymbol, String.CASE_INSENSITIVE_ORDER));
		}
		if (entity.getGeneToQtlCandidate() != null && !entity.getGeneToQtlCandidate().isEmpty()) {
    		Iterable<RelationshipMarkerQTLCandidateDomain> relationships = markerQTLCandidateTranslator.translateEntities(entity.getGeneToQtlCandidate());
			for (RelationshipMarkerQTLCandidateDomain i : relationships) {
				if (domain.getMarkerKey().equals(i.getObjectKey2())) {
					SlimMarkerDomain markerDomain = new SlimMarkerDomain();				
					markerDomain.setMarkerKey(i.getObjectKey1());
					markerDomain.setSymbol(i.getSymbol1() + " (" + i.getJnumid() + ")");
					markerqtlc.add(markerDomain);
				}
    		}
    		domain.setQtlCandidateToGene(markerqtlc);
			domain.getQtlCandidateToGene().sort(Comparator.comparing(SlimMarkerDomain::getSymbol, String.CASE_INSENSITIVE_ORDER));
		}
		
		// one-to-many qtl-interaction-to-gene relationships
		List<SlimMarkerDomain> markerqtli = new ArrayList<SlimMarkerDomain>();
		if (entity.getQtlInteractionToGene() != null && !entity.getQtlInteractionToGene().isEmpty()) {
    		Iterable<RelationshipMarkerQTLInteractionDomain> relationships = markerQTLInteractionTranslator.translateEntities(entity.getQtlInteractionToGene());
			for (RelationshipMarkerQTLInteractionDomain i : relationships) {
				if (domain.getMarkerKey().equals(i.getObjectKey1())) {
					SlimMarkerDomain markerDomain = new SlimMarkerDomain();				
					markerDomain.setMarkerKey(i.getObjectKey2());
					markerDomain.setSymbol(i.getSymbol2());
					log.info(markerDomain.getMarkerKey());
					log.info(markerDomain.getSymbol());
					log.info(markerqtli.contains(markerDomain));
					markerqtli.add(markerDomain);
				}
    		}
    		domain.setQtlInteractionToGene(markerqtli);
			domain.getQtlInteractionToGene().sort(Comparator.comparing(SlimMarkerDomain::getSymbol, String.CASE_INSENSITIVE_ORDER));
		}

		// accession ids editable for mouse
		if (entity.getOrganism().get_organism_key() == 1 && entity.getEditAccessionIdsMouse() != null && !entity.getEditAccessionIdsMouse().isEmpty()) {
			Iterable<AccessionDomain> acc = accessionTranslator.translateEntities(entity.getEditAccessionIdsMouse());
			domain.setEditAccessionIds(IteratorUtils.toList(acc.iterator()));
			domain.getEditAccessionIds().sort(Comparator.comparing(AccessionDomain::getLogicaldb).thenComparing(AccessionDomain::getAccID));
		}
		// accession ids editable for non-mouse;  exclude organisms used in entrezload
		else if (entity.getOrganism().get_organism_key() != 1
				&& entity.getOrganism().get_organism_key() != 2
				&& entity.getOrganism().get_organism_key() != 10
				&& entity.getOrganism().get_organism_key() != 11
				&& entity.getOrganism().get_organism_key() != 13
				&& entity.getOrganism().get_organism_key() != 40
				&& entity.getOrganism().get_organism_key() != 63
				&& entity.getOrganism().get_organism_key() != 84
				&& entity.getOrganism().get_organism_key() != 86				
				&& entity.getOrganism().get_organism_key() != 94
				&& entity.getOrganism().get_organism_key() != 95
				&& entity.getEditAccessionIdsNonMouse() != null 
				&& !entity.getEditAccessionIdsNonMouse().isEmpty()) {
			Iterable<AccessionDomain> acc = accessionTranslator.translateEntities(entity.getEditAccessionIdsNonMouse());
			domain.setEditAccessionIds(IteratorUtils.toList(acc.iterator()));
			domain.getEditAccessionIds().sort(Comparator.comparing(AccessionDomain::getLogicaldb).thenComparing(AccessionDomain::getAccID));
		}		
		
		// accession ids non-editable for mouse
		if (entity.getOrganism().get_organism_key() == 1 && entity.getNonEditAccessionIdsMouse() != null && !entity.getNonEditAccessionIdsMouse().isEmpty()) {
			Iterable<AccessionDomain> acc = accessionTranslator.translateEntities(entity.getNonEditAccessionIdsMouse());
			domain.setNonEditAccessionIds(IteratorUtils.toList(acc.iterator()));
			domain.getNonEditAccessionIds().sort(Comparator.comparing(AccessionDomain::getLogicaldb).thenComparing(AccessionDomain::getAccID));
		}
		// accession ids non-editable for non-mouse
		else if (entity.getOrganism().get_organism_key() != 1 && entity.getNonEditAccessionIdsNonMouse() != null && !entity.getNonEditAccessionIdsNonMouse().isEmpty()) {		
			Iterable<AccessionDomain> acc = accessionTranslator.translateEntities(entity.getNonEditAccessionIdsNonMouse());
			domain.setNonEditAccessionIds(IteratorUtils.toList(acc.iterator()));
			domain.getNonEditAccessionIds().sort(Comparator.comparing(AccessionDomain::getLogicaldb).thenComparing(AccessionDomain::getAccID));
		}
		
		// biotypes 
		if (entity.getBiotypes() != null && !entity.getBiotypes().isEmpty()) {
			log.debug("size of entity.getBiotypes:" + entity.getBiotypes().size());
			Iterable<SeqMarkerBiotypeDomain> bio = biotypeTranslator.translateEntities(entity.getBiotypes());
			domain.setBiotypes(IteratorUtils.toList(bio.iterator()));
			domain.getBiotypes().sort(Comparator.comparing(SeqMarkerBiotypeDomain::getRawbiotype, String.CASE_INSENSITIVE_ORDER));
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
