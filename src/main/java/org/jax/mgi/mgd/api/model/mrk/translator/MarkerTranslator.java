package org.jax.mgi.mgd.api.model.mrk.translator;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.acc.domain.AccessionDomain;
import org.jax.mgi.mgd.api.model.acc.translator.AccessionTranslator;
import org.jax.mgi.mgd.api.model.mgi.domain.MGISynonymDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.NoteDomain;
import org.jax.mgi.mgd.api.model.mgi.translator.MGISynonymTranslator;
import org.jax.mgi.mgd.api.model.mgi.translator.NoteTranslator;
import org.jax.mgi.mgd.api.model.mrk.domain.MarkerDomain;
import org.jax.mgi.mgd.api.model.mrk.domain.MarkerHistoryDomain;
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

	@Override
	protected MarkerDomain entityToDomain(Marker entity, int translationDepth) {
		
		MarkerDomain domain = new MarkerDomain();

		// do not use 'processStatus' because this is a master domain
		// and only 1 master domain record is processed by the create/update endpoint
		
		domain.setMarkerKey(String.valueOf(entity.get_marker_key()));
		domain.setSymbol(entity.getSymbol());
		domain.setName(entity.getName());
		domain.setChromosome(entity.getChromosome());
		domain.setCytogeneticOffset(entity.getCytogeneticOffset());
		domain.setCmOffset(entity.getCmOffset().toString());
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
		if (entity.getEditorNote() != null) {
			Iterable<NoteDomain> editorNote = noteTranslator.translateEntities(entity.getEditorNote());
			if(editorNote.iterator().hasNext() == true) {
				domain.setEditorNote(editorNote.iterator().next());
			}
		}

		// at most one sequenceNote
		if (entity.getSequenceNote() != null) {
			Iterable<NoteDomain> sequenceNote = noteTranslator.translateEntities(entity.getSequenceNote());
			if(sequenceNote.iterator().hasNext() == true) {
				domain.setSequenceNote(sequenceNote.iterator().next());
			}
		}
		
		// at most one revisionNote
		if (entity.getRevisionNote() != null) {
			Iterable<NoteDomain> revisionNote = noteTranslator.translateEntities(entity.getRevisionNote());
			if(revisionNote.iterator().hasNext() == true) {
				domain.setRevisionNote(revisionNote.iterator().next());
			}
		}
		
		// at most one strainNote
		if (entity.getStrainNote() != null) {
			Iterable<NoteDomain> strainNote = noteTranslator.translateEntities(entity.getStrainNote());
			if(strainNote.iterator().hasNext() == true) {
				domain.setStrainNote(strainNote.iterator().next());
			}
		}
		
		// at most one locationNote
		if (entity.getLocationNote() != null) {
			Iterable<NoteDomain> locationNote = noteTranslator.translateEntities(entity.getLocationNote());
			if(locationNote.iterator().hasNext() == true) {
			domain.setLocationNote(locationNote.iterator().next());
			}
		}
		
		// mgi accession ids only
		if (entity.getMgiAccessionIds() != null) {
			Iterable<AccessionDomain> acc = accessionTranslator.translateEntities(entity.getMgiAccessionIds());
			if(acc.iterator().hasNext() == true) {
				domain.setMgiAccessionIds(IteratorUtils.toList(acc.iterator()));
			}
		}
		
		// one-to-many marker history
		if (entity.getHistory() != null) {
			Iterable<MarkerHistoryDomain> i = historyTranslator.translateEntities(entity.getHistory());
			if(i.iterator().hasNext() == true) {
				domain.setHistory(IteratorUtils.toList(i.iterator()));
			}
		}
		
		// one-to-many marker synonyms
		if (entity.getSynonyms() != null) {
			Iterable<MGISynonymDomain> i = synonymTranslator.translateEntities(entity.getSynonyms());
			if(i.iterator().hasNext() == true) {
				domain.setSynonyms(IteratorUtils.toList(i.iterator()));
			}
		}

		// one-to-many marker feature types
		// featureTypes can also be set via voc/service/Annotation.service/markerFeatureTypes
		if (entity.getFeatureTypes() != null) {
			Iterable<MarkerFeatureTypeDomain> i = featureTypeTranslator.translateEntities(entity.getFeatureTypes());
			if(i.iterator().hasNext() == true) {
				domain.setFeatureTypes(IteratorUtils.toList(i.iterator()));
			}
		}
		
		// one-to-many gene-to-tss relationships
		if (entity.getGeneToTss() != null) {
			Iterable<SlimMarkerDomain> i = slimMarkerTranslator.translateEntities(entity.getGeneToTss());
			if(i.iterator().hasNext() == true) {
				domain.setGeneToTss(IteratorUtils.toList(i.iterator()));
			}
		}

		// one-to-many tss-to-gene relationships
		if (entity.getTssToGene() != null) {
			Iterable<SlimMarkerDomain> i = slimMarkerTranslator.translateEntities(entity.getTssToGene());
			if(i.iterator().hasNext() == true) {
				domain.setTssToGene(IteratorUtils.toList(i.iterator()));
			}
		}
				
		// one-to-many marker aliases
		if (entity.getAliases() != null) {
			Iterable<SlimMarkerDomain> i = slimMarkerTranslator.translateEntities(entity.getAliases());
			if(i.iterator().hasNext() == true) {
				domain.setAliases(IteratorUtils.toList(i.iterator()));
			}
		}

		// accession ids editable
		if (entity.getEditAccessionIds() != null) {
			Iterable<AccessionDomain> acc = accessionTranslator.translateEntities(entity.getEditAccessionIds());
			if(acc.iterator().hasNext() == true) {
				domain.setEditAccessionIds(IteratorUtils.toList(acc.iterator()));
			}
		}
		
		// accession ids non-editable 
		//
		if (entity.getNonEditAccessionIds() != null) {
			Iterable<AccessionDomain> acc = accessionTranslator.translateEntities(entity.getNonEditAccessionIds());
			if(acc.iterator().hasNext() == true) {
				domain.setNonEditAccessionIds(IteratorUtils.toList(acc.iterator()));
			}
		}
		
		// these domains are only set by individual object endpoints
		// that is, see acc/service/AccessionService:getMarkerEditAccessionIds
							
		// reference associations
		//if (entity.getRefAssocs() != null) {
		//	MGIReferenceAssocTranslator refAssocTranslator = new MGIReferenceAssocTranslator();
		//	Iterable<MGIReferenceAssocDomain> i = refAssocTranslator.translateEntities(entity.getRefAssocs());
		//	if(i.iterator().hasNext() == true) {
		//		domain.setRefAssocs(IteratorUtils.toList(i.iterator()));
		//	}
		//}						
		
		return domain;
	}

	@Override
	protected Marker domainToEntity(MarkerDomain domain, int translationDepth) {
		// TODO Auto-generated method stub
		return null;
	}

}
