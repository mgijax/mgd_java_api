package org.jax.mgi.mgd.api.model.prb.translator;

import java.util.Comparator;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.acc.domain.SlimAccessionDomain;
import org.jax.mgi.mgd.api.model.acc.translator.SlimAccessionTranslator;
import org.jax.mgi.mgd.api.model.mgi.domain.NoteDomain;
import org.jax.mgi.mgd.api.model.mgi.translator.NoteTranslator;
import org.jax.mgi.mgd.api.model.prb.domain.ProbeDomain;
import org.jax.mgi.mgd.api.model.prb.domain.ProbeMarkerDomain;
import org.jax.mgi.mgd.api.model.prb.domain.ProbeNoteDomain;
import org.jax.mgi.mgd.api.model.prb.domain.ProbeReferenceDomain;
import org.jax.mgi.mgd.api.model.prb.entities.Probe;
import org.jboss.logging.Logger;

public class ProbeTranslator extends BaseEntityDomainTranslator<Probe, ProbeDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Override
	protected ProbeDomain entityToDomain(Probe entity) {
		
		ProbeDomain domain = new ProbeDomain();
		
		domain.setProbeKey(String.valueOf(entity.get_probe_key()));
		domain.setName(entity.getName());
		domain.setSegmentTypeKey(String.valueOf(entity.getSegmentType().get_term_key()));
		domain.setSegmentType(entity.getSegmentType().getTerm());
		domain.setVectorTypeKey(String.valueOf(entity.getVectorType().get_term_key()));
		domain.setVectorType(entity.getVectorType().getTerm());
		domain.setPrimer1sequence(entity.getPrimer1sequence());
		domain.setPrimer2sequence(entity.getPrimer2sequence());
		domain.setRegionCovered(entity.getRegionCovered());
		domain.setInsertSite(entity.getInsertSite());
		domain.setInsertSize(entity.getInsertSize());
		domain.setProductSize(entity.getProductSize());
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));

		// mgi accession ids only
		if (entity.getMgiAccessionIds() != null && !entity.getMgiAccessionIds().isEmpty()) {
			domain.setAccID(entity.getMgiAccessionIds().get(0).getAccID());
			SlimAccessionTranslator accessionTranslator = new SlimAccessionTranslator();			
			Iterable<SlimAccessionDomain> acc = accessionTranslator.translateEntities(entity.getMgiAccessionIds());
			domain.setMgiAccessionIds(IteratorUtils.toList(acc.iterator()));
		}
		
		// probe source
		ProbeSourceTranslator probesourceTranslator = new ProbeSourceTranslator();
		domain.setProbeSource(probesourceTranslator.entityToDomain(entity.getProbeSource()));
		
		// at most one derived-from
		if (entity.getDerivedFrom() != null && !entity.getDerivedFrom().getName().isEmpty()) {
			domain.setDerivedFromKey(String.valueOf(entity.getDerivedFrom().get_probe_key()));
			domain.setDerivedFromName(entity.getDerivedFrom().getName());
			domain.setDerivedFromAccID(entity.getDerivedFrom().getMgiAccessionIds().get(0).getAccID());
		}
		
		// at most one amp-primer
		if (entity.getAmpPrimer() != null && !entity.getAmpPrimer().getName().isEmpty()) {
			domain.setAmpPrimerKey(String.valueOf(entity.getAmpPrimer().get_probe_key()));
			domain.setAmpPrimerName(entity.getAmpPrimer().getName());
			domain.setAmpPrimerAccID(entity.getAmpPrimer().getMgiAccessionIds().get(0).getAccID());
		}
		
		// markers
		if (entity.getMarkers() != null && !entity.getMarkers().isEmpty()) {
			ProbeMarkerTranslator markerTranslator = new ProbeMarkerTranslator();
			Iterable<ProbeMarkerDomain> i = markerTranslator.translateEntities(entity.getMarkers());
			domain.setMarkers(IteratorUtils.toList(i.iterator()));
			domain.getMarkers().sort(Comparator.comparing(ProbeMarkerDomain::getMarkerSymbol).thenComparing(ProbeMarkerDomain::getJnum));			
		}

		// references
		if (entity.getReferences() != null && !entity.getReferences().isEmpty()) {
			ProbeReferenceTranslator referenceTranslator = new ProbeReferenceTranslator();
			Iterable<ProbeReferenceDomain> i = referenceTranslator.translateEntities(entity.getReferences());
			domain.setReferences(IteratorUtils.toList(i.iterator()));
			domain.getReferences().sort(Comparator.comparing(ProbeReferenceDomain::getJnum));			
		}
		
		// at most one note
		if (entity.getGeneralNote() != null && !entity.getGeneralNote().isEmpty()) {
			ProbeNoteTranslator noteTranslator = new ProbeNoteTranslator();
			Iterable<ProbeNoteDomain> note = noteTranslator.translateEntities(entity.getGeneralNote());
			domain.setGeneralNote(note.iterator().next());
		}

		// at most one sequenceNote
		if (entity.getRawsequenceNote() != null && !entity.getRawsequenceNote().isEmpty()) {
			NoteTranslator noteTranslator = new NoteTranslator();
			Iterable<NoteDomain> sequenceNote = noteTranslator.translateEntities(entity.getRawsequenceNote());
			domain.setRawsequenceNote(sequenceNote.iterator().next());
		}
		
		return domain;
	}

}
