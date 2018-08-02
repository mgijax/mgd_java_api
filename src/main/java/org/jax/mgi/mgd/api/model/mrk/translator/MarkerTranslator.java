package org.jax.mgi.mgd.api.model.mrk.translator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.acc.entities.Accession;
import org.jax.mgi.mgd.api.model.all.domain.AlleleDomain;
import org.jax.mgi.mgd.api.model.all.translator.AlleleTranslator;
import org.jax.mgi.mgd.api.model.bib.translator.ReferenceTranslator;
import org.jax.mgi.mgd.api.model.gxd.domain.AntibodyDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.AssayDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.IndexDomain;
import org.jax.mgi.mgd.api.model.gxd.translator.AntibodyTranslator;
import org.jax.mgi.mgd.api.model.gxd.translator.AssayTranslator;
import org.jax.mgi.mgd.api.model.gxd.translator.IndexTranslator;
import org.jax.mgi.mgd.api.model.mgi.domain.NoteDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.MGISynonym;
import org.jax.mgi.mgd.api.model.mgi.entities.Relationship;
import org.jax.mgi.mgd.api.model.mgi.translator.NoteTranslator;
import org.jax.mgi.mgd.api.model.mld.translator.ExperimentTranslator;
import org.jax.mgi.mgd.api.model.mrk.domain.MarkerDomain;
import org.jax.mgi.mgd.api.model.mrk.entities.Marker;
import org.jax.mgi.mgd.api.model.mrk.entities.MarkerMCVCache;
import org.jax.mgi.mgd.api.model.prb.translator.ProbeTranslator;
import org.jax.mgi.mgd.api.model.seq.translator.SequenceTranslator;
import org.jax.mgi.mgd.api.model.voc.domain.TermDomain;
import org.jax.mgi.mgd.api.model.voc.translator.TermTranslator;

public class MarkerTranslator extends BaseEntityDomainTranslator<Marker, MarkerDomain> {

	private AlleleTranslator alleleTranslator = new AlleleTranslator();
	private AntibodyTranslator antibodyTranslator = new AntibodyTranslator();
	private AssayTranslator assayTranslator = new AssayTranslator();
	private ExperimentTranslator exptTranslator = new ExperimentTranslator();
	private IndexTranslator indexTranslator = new IndexTranslator();
	private NoteTranslator noteTranslator = new NoteTranslator();
	private ProbeTranslator probeTranslator = new ProbeTranslator();
	private ReferenceTranslator referenceTranslator = new ReferenceTranslator();
	private SequenceTranslator sequenceTranslator = new SequenceTranslator();
	private TermTranslator termTranslator = new TermTranslator();

	@Override
	protected MarkerDomain entityToDomain(Marker entity, int translationDepth) {
		MarkerDomain domain = new MarkerDomain();
		domain.setMarkerKey(entity.get_marker_key());
		domain.setSymbol(entity.getSymbol());
		domain.setName(entity.getName());
		domain.setChromosome(entity.getChromosome());
		domain.setCytogeneticOffset(entity.getCytogeneticOffset());
		domain.setOrganism(entity.getOrganism().getCommonname());
		domain.setMarkerStatus(entity.getMarkerStatus().getStatus());
		domain.setMarkerType(entity.getMarkerType().getName());
		domain.setCreatedBy(entity.getCreatedBy().getName());
		domain.setModifiedBy(entity.getModifiedBy().getName());
		domain.setCreation_date(entity.getCreation_date());
		domain.setModification_date(entity.getModification_date());
		domain.setMgiAccessionId(entity.getMgiAccessionId().getAccID());

		if(translationDepth > 0) {

			// more Marker details
			
			// at most one marker note
			if(entity.getMarkerNote() != null) {
				domain.setMarkerNote(entity.getMarkerNote().getNote());
			}
		
			// at most one set of location info
			if(entity.getMarkerLocation() != null) {
				String addProvider = "";
			
				domain.setLocationChromosome(entity.getMarkerLocation().getGenomicChromosome());
				domain.setLocationStartCoordinate(entity.getMarkerLocation().getStartCoordinate());
				domain.setLocationEndCoordinate(entity.getMarkerLocation().getEndCoordinate());
				domain.setLocationStrand(entity.getMarkerLocation().getStrand());
				domain.setLocationMapUnits(entity.getMarkerLocation().getMapUnits());
				domain.setLocationProvider(entity.getMarkerLocation().getProvider());
				domain.setLocationVersion(entity.getMarkerLocation().getVersion());
			
				if(domain.getLocationProvider() != null) {
					addProvider = " From " + domain.getLocationProvider() 
						+ " annotation of " + domain.getLocationVersion();
				}
				if(domain.getLocationStartCoordinate() == null | domain.getLocationEndCoordinate() == null ) {
					domain.setLocationText("Chr" + domain.getChromosome() 
						+ addProvider);
				}
				else {
					domain.setLocationText("Chr" + domain.getLocationChromosome() + ":"
						+ domain.getLocationStartCoordinate() + "-"
						+ domain.getLocationEndCoordinate() + " bp, "
						+ domain.getLocationStrand() + " strand"
						+ addProvider
						);
				}
			}
				
			// at most one locationNote
			Iterable<NoteDomain> locationNotes = noteTranslator.translateEntities(entity.getLocationNotes(), translationDepth - 1);
			if(locationNotes.iterator().hasNext() == true) {
				domain.setLocationNote(locationNotes.iterator().next().getNoteChunk());
			}
			
			// all synonym objects
			List<String> synonyms = new ArrayList<String>();
			for (MGISynonym ms : entity.getSynonyms()) {
				synonyms.add(ms.getSynonym());
			}
			Collections.sort(synonyms);
			domain.setSynonyms(synonyms);

			// all gene-to-tss relationships
			List<String> geneToTssRelationships = new ArrayList<String>();
			for (Relationship ms : entity.getGeneToTssRelationships()) {
				geneToTssRelationships.add(ms.getTssSymbol().getSymbol());
			}
			Collections.sort(geneToTssRelationships);
			domain.setGeneToTssRelationships(geneToTssRelationships);
			
			// all tss-to-gene relationships
			List<String> tssToGeneRelationships = new ArrayList<String>();
			for (Relationship ms : entity.getTssToGeneRelationships()) {
				tssToGeneRelationships.add(ms.getGeneSymbol().getSymbol());
			}
			Collections.sort(tssToGeneRelationships);
			domain.setTssToGeneRelationships(tssToGeneRelationships);
			
			// secondary ids
			List<String> secondaryMgiIds = new ArrayList<String>();
			for (Accession sa : entity.getSecondaryMgiAccessionIds()) {
				secondaryMgiIds.add(sa.getAccID());
			}
			Collections.sort(secondaryMgiIds);
			domain.setSecondaryMgiIds(secondaryMgiIds);
			
			// at most one mcvTerm
			List<TermDomain> mcvTerms = new ArrayList<TermDomain>();
			for (MarkerMCVCache mm : entity.getMcvTerms()) {
				mcvTerms.add(termTranslator.translate(mm.getMcvTerm(), translationDepth - 1));
			}
			if(mcvTerms.size() > 0) {
				domain.setMcvTerm(mcvTerms.get(0).getTerm());
			}
			
			// Summary links : by using counts
			
			if(entity.getAlleles().isEmpty() == false) {
				domain.setHasAlleles(true);
			}
			
			if(entity.getAntibodies().isEmpty() == false) {
				domain.setHasAntibodies(true);
			}
			
			if(entity.getAssays().isEmpty() == false) {
				domain.setHasAssays(true);
			}
			
			if(entity.getIndexes().isEmpty() == false) {
				domain.setHasIndexes(true);
			}
			
			if(entity.getExptMarkers().isEmpty() == false) {
				domain.setHasExperiments(true);
			}
			
			if(entity.getProbeMarkers().isEmpty() == false) {
				domain.setHasProbes(true);
			}
			
			//if(entity.getMarkerReferenceCache().isEmpty() == false) {
				//domain.setHasReferences(true);
			//}
			
			if(entity.getSequenceMarkers().isEmpty() == false) {
				domain.setHasSequences(true);
			}
		}
		
		return domain;
	}

	@Override
	protected Marker domainToEntity(MarkerDomain domain, int translationDepth) {
		// TODO Auto-generated method stub
		return null;
	}

}
