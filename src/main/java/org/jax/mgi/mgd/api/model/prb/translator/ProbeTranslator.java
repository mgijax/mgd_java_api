package org.jax.mgi.mgd.api.model.prb.translator;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.mgi.domain.NoteDomain;
import org.jax.mgi.mgd.api.model.mgi.translator.NoteTranslator;
import org.jax.mgi.mgd.api.model.prb.domain.ProbeDomain;
import org.jax.mgi.mgd.api.model.prb.domain.ProbeMarkerDomain;
import org.jax.mgi.mgd.api.model.prb.entities.Probe;

public class ProbeTranslator extends BaseEntityDomainTranslator<Probe, ProbeDomain> {

	@Override
	protected ProbeDomain entityToDomain(Probe entity) {
		
		ProbeDomain domain = new ProbeDomain();
		
		domain.setProbeKey(String.valueOf(entity.get_probe_key()));
		domain.setName(entity.getName());
		domain.setDerivedFrom(String.valueOf(entity.getDerivedFrom()));
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

		// markers
		if (entity.getMarkers() != null && !entity.getMarkers().isEmpty()) {
			ProbeMarkerTranslator markerTranslator =new ProbeMarkerTranslator();
			Iterable<ProbeMarkerDomain> i = markerTranslator.translateEntities(entity.getMarkers());
			domain.setMarkers(IteratorUtils.toList(i.iterator()));
		}
		
		// at most one note
		if (entity.getGeneralNote() != null && !entity.getGeneralNote().isEmpty()) {
			NoteTranslator noteTranslator = new NoteTranslator();
			Iterable<NoteDomain> note = noteTranslator.translateEntities(entity.getGeneralNote());
			domain.setGeneralNote(note.iterator().next());
		}				
		
		return domain;
	}

}
