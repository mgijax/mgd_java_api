package org.jax.mgi.mgd.api.model.mld.translator;

import java.util.Comparator;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.acc.domain.SlimAccessionDomain;
import org.jax.mgi.mgd.api.model.acc.translator.SlimAccessionTranslator;
import org.jax.mgi.mgd.api.model.mgi.domain.MGISynonymDomain;
import org.jax.mgi.mgd.api.model.mld.domain.ExptMarkerDomain;
import org.jax.mgi.mgd.api.model.mld.domain.ExptNoteDomain;
import org.jax.mgi.mgd.api.model.mld.domain.ExptsDomain;
import org.jax.mgi.mgd.api.model.mld.domain.MappingNoteDomain;
import org.jax.mgi.mgd.api.model.mld.entities.Expts;
import org.jboss.logging.Logger;

public class ExptsTranslator extends BaseEntityDomainTranslator<Expts, ExptsDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Override
	protected ExptsDomain entityToDomain(Expts entity) {
		
		ExptsDomain domain = new ExptsDomain();

		domain.setExptKey(String.valueOf(entity.get_expt_key()));
		domain.setExptDisplay(entity.getReference().getJnumid() + ", " + entity.getExptType() + ", Chr " + entity.getChromosome());
		domain.setExptType(entity.getExptType());
		domain.setChromosome(entity.getChromosome());
		domain.setRefsKey(String.valueOf(entity.getReference().get_refs_key()));
		domain.setJnumid(entity.getReference().getJnumid());
		domain.setJnum(entity.getReference().getNumericPart());
		domain.setShort_citation(entity.getReference().getShort_citation());		
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));

		// mgi accession ids only
		if (entity.getMgiAccessionIds() != null && !entity.getMgiAccessionIds().isEmpty()) {
			domain.setAccID(entity.getMgiAccessionIds().get(0).getAccID());
			SlimAccessionTranslator accessionTranslator = new SlimAccessionTranslator();			
			Iterable<SlimAccessionDomain> acc = accessionTranslator.translateEntities(entity.getMgiAccessionIds());
			domain.setMgiAccessionIds(IteratorUtils.toList(acc.iterator()));
		}
	
		// reference note
		if (entity.getReference().getMappingNote() != null && !entity.getReference().getMappingNote().isEmpty()) {
			MappingNoteTranslator noteTranslator = new MappingNoteTranslator();
			Iterable<MappingNoteDomain> note = noteTranslator.translateEntities(entity.getReference().getMappingNote());
			domain.setReferenceNote(note.iterator().next());
		}

		// markers
		if (entity.getMarkers() != null && !entity.getMarkers().isEmpty()) {
			ExptMarkerTranslator markerTranslator = new ExptMarkerTranslator();			
			Iterable<ExptMarkerDomain> m = markerTranslator.translateEntities(entity.getMarkers());
			domain.setMarkers(IteratorUtils.toList(m.iterator()));
			domain.getMarkers().sort(Comparator.comparing(ExptMarkerDomain::getMarkerSymbol, String.CASE_INSENSITIVE_ORDER));		
		}
		
		// at most one exptNote
		if (entity.getExptNote() != null && !entity.getExptNote().isEmpty()) {
			ExptNoteTranslator noteTranslator = new ExptNoteTranslator();			
			Iterable<ExptNoteDomain> note = noteTranslator.translateEntities(entity.getExptNote());
			domain.setExptNote(note.iterator().next());
		}
		
		return domain;
	}

}
