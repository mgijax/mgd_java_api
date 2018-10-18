package org.jax.mgi.mgd.api.model.mrk.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.mgi.domain.NoteDomain;
import org.jax.mgi.mgd.api.model.mgi.translator.NoteTranslator;
import org.jax.mgi.mgd.api.model.mrk.domain.MarkerDomain;
import org.jax.mgi.mgd.api.model.mrk.entities.Marker;

public class MarkerTranslator extends BaseEntityDomainTranslator<Marker, MarkerDomain> {

	private NoteTranslator noteTranslator = new NoteTranslator();
	//private TermTranslator termTranslator = new TermTranslator();
	//private SequenceMarkerCacheTranslator biotypesTranslator = new SequenceMarkerCacheTranslator();

	@Override
	protected MarkerDomain entityToDomain(Marker entity, int translationDepth) {
		MarkerDomain domain = new MarkerDomain();
		domain.setMarkerKey(entity.get_marker_key());
		domain.setSymbol(entity.getSymbol());
		domain.setName(entity.getName());
		domain.setChromosome(entity.getChromosome());
		domain.setCytogeneticOffset(entity.getCytogeneticOffset());
		domain.setCmOffset(entity.getCmOffset());
		domain.setOrganismKey(entity.getOrganism().get_organism_key());
		domain.setOrganism(entity.getOrganism().getCommonname());
		domain.setMarkerStatusKey(entity.getMarkerStatus().get_marker_status_key());
		domain.setMarkerStatus(entity.getMarkerStatus().getStatus());
		domain.setMarkerTypeKey(entity.getMarkerType().get_marker_type_key());
		domain.setMarkerType(entity.getMarkerType().getName());
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key());
		domain.setCreatedBy(entity.getCreatedBy().getName());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key());
		domain.setModifiedBy(entity.getModifiedBy().getName());
		domain.setCreation_date(entity.getCreation_date());
		domain.setModification_date(entity.getModification_date());
		
		// at most one editorNote
		Iterable<NoteDomain> editorNote = noteTranslator.translateEntities(entity.getEditorNote(), translationDepth - 1);
		if(editorNote.iterator().hasNext() == true) {
			domain.setEditorNote(editorNote.iterator().next().getNoteChunk());
		}
		
		// at most one sequenceNote
		Iterable<NoteDomain> sequenceNote = noteTranslator.translateEntities(entity.getSequenceNote(), translationDepth - 1);
		if(sequenceNote.iterator().hasNext() == true) {
			domain.setSequenceNote(sequenceNote.iterator().next().getNoteChunk());
		}
		// at most one revisionNote
		Iterable<NoteDomain> revisionNote = noteTranslator.translateEntities(entity.getRevisionNote(), translationDepth - 1);
		if(revisionNote.iterator().hasNext() == true) {
			domain.setRevisionNote(revisionNote.iterator().next().getNoteChunk());
		}
		// at most one strainNote
		Iterable<NoteDomain> strainNote = noteTranslator.translateEntities(entity.getStrainNote(), translationDepth - 1);
		if(strainNote.iterator().hasNext() == true) {
			domain.setStrainNote(strainNote.iterator().next().getNoteChunk());
		}
		
		// at most one locationNote
		Iterable<NoteDomain> locationNote = noteTranslator.translateEntities(entity.getLocationNote(), translationDepth - 1);
		if(locationNote.iterator().hasNext() == true) {
			domain.setLocationNote(locationNote.iterator().next().getNoteChunk());
		}
		
		//domain.setMgiAccessionId(entity.getMgiAccessionId().getAccID());

		// at most one marker note
		//if(entity.getMarkerNote() != null) {
			//domain.setMarkerNote(entity.getMarkerNote().getNote());
		//}
	
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
