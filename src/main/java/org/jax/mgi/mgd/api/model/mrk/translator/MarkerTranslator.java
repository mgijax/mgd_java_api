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
import org.jax.mgi.mgd.api.model.mrk.entities.Marker;
import org.jax.mgi.mgd.api.model.mrk.service.MarkerService;
import org.jboss.logging.Logger;

public class MarkerTranslator extends BaseEntityDomainTranslator<Marker, MarkerDomain> {

	protected Logger log = Logger.getLogger(MarkerService.class);
	
	private NoteTranslator noteTranslator = new NoteTranslator();
	private AccessionTranslator accessionTranslator = new AccessionTranslator();
	private MarkerHistoryTranslator historyTranslator = new MarkerHistoryTranslator();
	private MGISynonymTranslator synonymTranslator = new MGISynonymTranslator();
	
	//private TermTranslator termTranslator = new TermTranslator();
	//private SequenceMarkerCacheTranslator biotypesTranslator = new SequenceMarkerCacheTranslator();

	@Override
	protected MarkerDomain entityToDomain(Marker entity, int translationDepth) {
			
		MarkerDomain domain = new MarkerDomain();
		
		domain.setMarkerKey(String.valueOf(entity.get_marker_key()));
		domain.setSymbol(entity.getSymbol());
		domain.setName(entity.getName());
		domain.setChromosome(entity.getChromosome());
		domain.setCytogeneticOffset(entity.getCytogeneticOffset());
		domain.setCmOffset(entity.getCmOffset().toString());
		domain.setOrganismKey(entity.getOrganism().get_organism_key().toString());
		domain.setOrganism(entity.getOrganism().getCommonname());
		domain.setMarkerStatusKey(entity.getMarkerStatus().get_marker_status_key().toString());
		domain.setMarkerStatus(entity.getMarkerStatus().getStatus());
		domain.setMarkerTypeKey(entity.getMarkerType().get_marker_type_key().toString());
		domain.setMarkerType(entity.getMarkerType().getName());
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setCreatedBy(entity.getCreatedBy().getName());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setModifiedBy(entity.getModifiedBy().getName());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
		
		// at most one editorNote
		if (entity.getEditorNote() != null) {
			Iterable<NoteDomain> editorNote = noteTranslator.translateEntities(entity.getEditorNote(), translationDepth - 1);
			if(editorNote.iterator().hasNext() == true) {
				domain.setEditorNote(editorNote.iterator().next());
			}
		}

		// at most one sequenceNote
		if (entity.getSequenceNote() != null) {
			Iterable<NoteDomain> sequenceNote = noteTranslator.translateEntities(entity.getSequenceNote(), translationDepth - 1);
			if(sequenceNote.iterator().hasNext() == true) {
				domain.setSequenceNote(sequenceNote.iterator().next());
			}
		}
		
		// at most one revisionNote
		if (entity.getRevisionNote() != null) {
			Iterable<NoteDomain> revisionNote = noteTranslator.translateEntities(entity.getRevisionNote(), translationDepth - 1);
			if(revisionNote.iterator().hasNext() == true) {
				domain.setRevisionNote(revisionNote.iterator().next());
			}
		}
		
		// at most one strainNote
		if (entity.getStrainNote() != null) {
			Iterable<NoteDomain> strainNote = noteTranslator.translateEntities(entity.getStrainNote(), translationDepth - 1);
			if(strainNote.iterator().hasNext() == true) {
				domain.setStrainNote(strainNote.iterator().next());
			}
		}
		
		// at most one locationNote
		if (entity.getLocationNote() != null) {
			Iterable<NoteDomain> locationNote = noteTranslator.translateEntities(entity.getLocationNote(), translationDepth - 1);
			if(locationNote.iterator().hasNext() == true) {
			domain.setLocationNote(locationNote.iterator().next());
			}
		}
		
		// mgi accession ids only
		if (entity.getMgiAccessionIds() != null) {
			Iterable<AccessionDomain> mgiAccessionIds = accessionTranslator.translateEntities(entity.getMgiAccessionIds(), translationDepth - 1);
			if(mgiAccessionIds.iterator().hasNext() == true) {
				domain.setMgiAccessionIds(IteratorUtils.toList(mgiAccessionIds.iterator()));
			}
		}
		
		// one-to-many marker history
		if (entity.getHistory() != null) {
			Iterable<MarkerHistoryDomain> h = historyTranslator.translateEntities(entity.getHistory(), translationDepth - 1);
			if(h.iterator().hasNext() == true) {
				domain.setHistory(IteratorUtils.toList(h.iterator()));
			}
		}
		
		// one-to-many marker synonyms
		if (entity.getSynonyms() != null) {
			Iterable<MGISynonymDomain> s = synonymTranslator.translateEntities(entity.getSynonyms(), translationDepth - 1);
			if(s.iterator().hasNext() == true) {
				domain.setSynonyms(IteratorUtils.toList(s.iterator()));
			}
		}
		
		// at most one set of location info
		//if(entity.getMarkerLocation() != null) {
			//String addProvider = "";
			
			//domain.setLocationChromosome(entity.getMarkerLocation().getGenomicChromosome());
			//domain.setLocationStartCoordinate(entity.getMarkerLocation().getStartCoordinate());
			//domain.setLocationEndCoordinate(entity.getMarkerLocation().getEndCoordinate());
			//domain.setLocationStrand(entity.getMarkerLocation().getStrand());
			//domain.setLocationMapUnits(entity.getMarkerLocation().getMapUnits());
			//domain.setLocationProvider(entity.getMarkerLocation().getProvider());
			//domain.setLocationVersion(entity.getMarkerLocation().getVersion());
			
			//if(domain.getLocationProvider() != null) {
				//addProvider = " From " + domain.getLocationProvider() 
					//+ " annotation of " + domain.getLocationVersion();
			//}
			//if(domain.getLocationStartCoordinate() == null | domain.getLocationEndCoordinate() == null ) {
				//domain.setLocationText("Chr" + domain.getChromosome() 
					//+ addProvider);
			//}
			//else {
			//	domain.setLocationText("Chr" + domain.getLocationChromosome() + ":"
			//		+ domain.getLocationStartCoordinate() + "-"
			//		+ domain.getLocationEndCoordinate() + " bp, "
			//		+ domain.getLocationStrand() + " strand"
			//		+ addProvider
			//		);
			//}
		//}
		
		// all synonym objects
//		List<String> synonyms = new ArrayList<String>();
//		for (MGISynonym ms : entity.getSynonyms()) {
//			synonyms.add(ms.getSynonym());
//		}
//		Collections.sort(synonyms);
//		domain.setSynonyms(synonyms);


		// all gene-to-tss relationships
		//List<String> geneToTssRelationships = new ArrayList<String>();
		//for (Relationship ms : entity.getGeneToTssRelationships()) {
		//	geneToTssRelationships.add(ms.getTssSymbol().getSymbol());
		//}
		//Collections.sort(geneToTssRelationships);
		//domain.setGeneToTssRelationships(geneToTssRelationships);
			
		// all tss-to-gene relationships
		//List<String> tssToGeneRelationships = new ArrayList<String>();
		//for (Relationship ms : entity.getTssToGeneRelationships()) {
		//	tssToGeneRelationships.add(ms.getGeneSymbol().getSymbol());
		//}
		//Collections.sort(tssToGeneRelationships);
		//domain.setTssToGeneRelationships(tssToGeneRelationships);
			
		// secondary ids
		//List<String> secondaryMgiIds = new ArrayList<String>();
		//for (Accession sa : entity.getSecondaryMgiAccessionIds()) {
		//	secondaryMgiIds.add(sa.getAccID());
		//}
		//Collections.sort(secondaryMgiIds);
		//domain.setSecondaryMgiIds(secondaryMgiIds);
			
		// at most one mcvTerm
		//List<TermDomain> mcvTerms = new ArrayList<TermDomain>();
		//for (MarkerMCVCache mm : entity.getMcvTerms()) {
		//	mcvTerms.add(termTranslator.translate(mm.getMcvTerm(), translationDepth - 1));
		//}
		//if(mcvTerms.size() > 0) {
		//	domain.setMcvTerm(mcvTerms.get(0).getTerm());
		//}
			
		// biotypes
		//Iterable<SequenceMarkerCacheDomain> biotypes = biotypesTranslator.translateEntities(entity.getBiotypes(), translationDepth - 1);
		//if(biotypes.iterator().hasNext() == true) {
		//	domain.setBiotypes(IteratorUtils.toList(biotypes.iterator()));
		//}
		
		return domain;
	}

	@Override
	protected Marker domainToEntity(MarkerDomain domain, int translationDepth) {
		// TODO Auto-generated method stub
		return null;
	}

}
