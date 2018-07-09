package org.jax.mgi.mgd.api.model.mrk.translator;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.all.domain.AlleleDomain;
import org.jax.mgi.mgd.api.model.all.translator.AlleleTranslator;
import org.jax.mgi.mgd.api.model.bib.domain.ReferenceDomain;
import org.jax.mgi.mgd.api.model.bib.entities.Reference;
import org.jax.mgi.mgd.api.model.bib.translator.ReferenceTranslator;
import org.jax.mgi.mgd.api.model.gxd.domain.AntibodyDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.AssayDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.IndexDomain;
import org.jax.mgi.mgd.api.model.gxd.translator.AntibodyTranslator;
import org.jax.mgi.mgd.api.model.gxd.translator.AssayTranslator;
import org.jax.mgi.mgd.api.model.gxd.translator.IndexTranslator;
import org.jax.mgi.mgd.api.model.mld.domain.ExperimentDomain;
import org.jax.mgi.mgd.api.model.mld.entities.ExptMarker;
import org.jax.mgi.mgd.api.model.mld.translator.ExperimentTranslator;
import org.jax.mgi.mgd.api.model.mrk.domain.MarkerDomain;
import org.jax.mgi.mgd.api.model.mrk.entities.Marker;
import org.jax.mgi.mgd.api.model.mrk.entities.MarkerMCVCache;
import org.jax.mgi.mgd.api.model.mrk.entities.MarkerReferenceCache;
import org.jax.mgi.mgd.api.model.prb.domain.ProbeDomain;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeMarker;
import org.jax.mgi.mgd.api.model.prb.translator.ProbeTranslator;
import org.jax.mgi.mgd.api.model.seq.domain.SequenceDomain;
import org.jax.mgi.mgd.api.model.seq.entities.SequenceMarkerCache;
import org.jax.mgi.mgd.api.model.seq.translator.SequenceTranslator;
import org.jax.mgi.mgd.api.model.voc.domain.TermDomain;
import org.jax.mgi.mgd.api.model.voc.translator.TermTranslator;

public class MarkerTranslator extends BaseEntityDomainTranslator<Marker, MarkerDomain> {

	private AlleleTranslator alleleTranslator = new AlleleTranslator();
	private AntibodyTranslator antibodyTranslator = new AntibodyTranslator();
	private AssayTranslator assayTranslator = new AssayTranslator();
	private ExperimentTranslator exptTranslator = new ExperimentTranslator();
	private IndexTranslator indexTranslator = new IndexTranslator();
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
		domain.setMarkerNote(entity.getMarkerNote().getNote());
		domain.setLocationChromosome(entity.getMarkerLocation().getGenomicChromosome());
		domain.setLocationStartCoordinate(entity.getMarkerLocation().getStartCoordinate());
		domain.setLocationEndCoordinate(entity.getMarkerLocation().getEndCoordinate());
		domain.setLocationStrand(entity.getMarkerLocation().getStrand());
		domain.setLocationMapUnits(entity.getMarkerLocation().getMapUnits());
		domain.setLocationProvider(entity.getMarkerLocation().getProvider());
		domain.setLocationVersion(entity.getMarkerLocation().getVersion());
		domain.setCreatedBy(entity.getCreatedBy().getName());
		domain.setModifiedBy(entity.getModifiedBy().getName());
		domain.setCreation_date(entity.getCreation_date());
		domain.setModification_date(entity.getModification_date());
		domain.setMgiAccessionId(entity.getMgiAccessionId().getAccID());

		if(translationDepth > 0) {
			
			Iterable<AlleleDomain> alleles = alleleTranslator.translateEntities(entity.getAlleles(), translationDepth - 1);
			domain.setAlleles(IteratorUtils.toList(alleles.iterator()));
			
			Iterable<AntibodyDomain> antibodies = antibodyTranslator.translateEntities(entity.getAntibodies(), translationDepth - 1);
			domain.setAntibodies(IteratorUtils.toList(antibodies.iterator()));
			
			Iterable<AssayDomain> assays = assayTranslator.translateEntities(entity.getAssays(), translationDepth - 1);
			domain.setAssays(IteratorUtils.toList(assays.iterator()));
			
			Iterable<IndexDomain> indexes = indexTranslator.translateEntities(entity.getIndexes(), translationDepth - 1);
			domain.setIndexes(IteratorUtils.toList(indexes.iterator()));
			
			List<ExperimentDomain> expts = new ArrayList<ExperimentDomain>();
			for (ExptMarker em : entity.getExptMarkers()) {
				expts.add(exptTranslator.translate(em.getExpt()));
			}
			domain.setExpts(expts);
			
			List<ProbeDomain> probes = new ArrayList<ProbeDomain>();
			for (ProbeMarker pm : entity.getProbeMarkers()) {
				probes.add(probeTranslator.translate(pm.getProbe()));
			}
			domain.setProbes(probes);
			
			List<String> references = new ArrayList<String>();
			
			for (MarkerReferenceCache mrc : entity.getReferenceMarkers()) {
				Reference r = mrc.getReference();
				ReferenceDomain rd = referenceTranslator.translate(r);
				references.add(rd.getJnumid());
				
			}
			domain.setReferences(references);
			
			List<SequenceDomain> sequences = new ArrayList<SequenceDomain>();
			for (SequenceMarkerCache sm : entity.getSequenceMarkers()) {
				sequences.add(sequenceTranslator.translate(sm.getSequence(), translationDepth -1));
			}
			domain.setSequences(sequences);
			
			List<TermDomain> mcvTerms = new ArrayList<TermDomain>();
			for (MarkerMCVCache mm : entity.getMcvTerms()) {
				mcvTerms.add(termTranslator.translate(mm.getMcvTerm(), translationDepth - 1));
			}
			domain.setMcvTerms(mcvTerms);
		}
		
		return domain;
	}

	@Override
	protected Marker domainToEntity(MarkerDomain domain, int translationDepth) {
		// TODO Auto-generated method stub
		return null;
	}

}
