package org.jax.mgi.mgd.api.model.prb.translator;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.mgi.domain.NoteDomain;
import org.jax.mgi.mgd.api.model.mgi.translator.NoteTranslator;
import org.jax.mgi.mgd.api.model.prb.domain.ProbeDomain;
import org.jax.mgi.mgd.api.model.prb.domain.ProbeMarkerDomain;
import org.jax.mgi.mgd.api.model.prb.domain.ProbeNoteDomain;
import org.jax.mgi.mgd.api.model.prb.domain.ProbeReferenceDomain;
import org.jax.mgi.mgd.api.model.prb.entities.Probe;
import org.jax.mgi.mgd.api.util.Constants;
import org.jboss.logging.Logger;

public class ProbeTranslator extends BaseEntityDomainTranslator<Probe, ProbeDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Override
	protected ProbeDomain entityToDomain(Probe entity) {
		
		ProbeDomain domain = new ProbeDomain();
		
		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
		domain.setProbeKey(String.valueOf(entity.get_probe_key()));
		domain.setName(entity.getName());
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
		}

		// at most one derived-from
		if (entity.getDerivedFrom() != null && !entity.getDerivedFrom().getName().isEmpty()) {
			log.info("translating getDerviedFrom");
			domain.setDerivedFromKey(String.valueOf(entity.getDerivedFrom().get_probe_key()));
			domain.setDerivedFromName(entity.getDerivedFrom().getName());
			domain.setDerivedFromAccID(entity.getDerivedFrom().getMgiAccessionIds().get(0).getAccID());
		}
		
		// markers
		if (entity.getMarkers() != null && !entity.getMarkers().isEmpty()) {
			ProbeMarkerTranslator markerTranslator =new ProbeMarkerTranslator();
			Iterable<ProbeMarkerDomain> i = markerTranslator.translateEntities(entity.getMarkers());
			domain.setMarkers(IteratorUtils.toList(i.iterator()));
		}

		// references
		if (entity.getReferences() != null && !entity.getReferences().isEmpty()) {
			ProbeReferenceTranslator referenceTranslator =new ProbeReferenceTranslator();
			Iterable<ProbeReferenceDomain> i = referenceTranslator.translateEntities(entity.getReferences());
			domain.setReferences(IteratorUtils.toList(i.iterator()));
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
