package org.jax.mgi.mgd.api.model.voc.translator;

import java.util.Collections;
import java.util.Comparator;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.mgi.domain.NoteDomain;
import org.jax.mgi.mgd.api.model.mgi.translator.NoteTranslator;
import org.jax.mgi.mgd.api.model.voc.domain.EvidenceDomain;
import org.jax.mgi.mgd.api.model.voc.domain.EvidencePropertyDomain;
import org.jax.mgi.mgd.api.model.voc.entities.Evidence;
import org.jax.mgi.mgd.api.util.Constants;
import org.jboss.logging.Logger;

public class EvidenceTranslator extends BaseEntityDomainTranslator<Evidence, EvidenceDomain> {
	
	protected Logger log = Logger.getLogger(getClass());

	private NoteTranslator noteTranslator = new NoteTranslator();		
	private EvidencePropertyTranslator propertyTranslator = new EvidencePropertyTranslator();
	
	@Override
	protected EvidenceDomain entityToDomain(Evidence entity) {
		EvidenceDomain domain = new EvidenceDomain();

		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
		domain.setAnnotEvidenceKey(String.valueOf(entity.get_annotevidence_key()));
		domain.setAnnotKey(String.valueOf(entity.get_annot_key()));
		domain.setEvidenceTermKey(String.valueOf(entity.getEvidenceTerm().get_term_key()));
		domain.setEvidenceTerm(entity.getEvidenceTerm().getTerm());
		domain.setEvidenceAbbreviation(entity.getEvidenceTerm().getAbbreviation());
		domain.setInferredFrom(entity.getInferredFrom());
		domain.setHasRGD("0");
		domain.setHasUniProt("0");
		
		if (!entity.getInferredFrom().isEmpty()) {
			if (entity.getInferredFrom().startsWith("RGD")) {
				domain.setHasRGD("1");
			}
			if (entity.getInferredFrom().startsWith("UniProtKB")) {
				domain.setHasUniProt("1");
			}
		}
		
		// reference can be null
		if (entity.getReference() != null) {
			domain.setRefsKey(String.valueOf(entity.getReference().get_refs_key()));
			domain.setJnumid(entity.getReference().getJnumid());
			domain.setJnum(String.valueOf(entity.getReference().getNumericPart()));
			domain.setShort_citation(entity.getReference().getShort_citation());
		}
		
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));		
			
		// properties
		if (entity.getProperties() != null && !entity.getProperties().isEmpty()) {
			Iterable<EvidencePropertyDomain> property = propertyTranslator.translateEntities(entity.getProperties());
			domain.setProperties(IteratorUtils.toList(property.iterator()));
			Comparator<EvidencePropertyDomain> compareByStanza = Comparator.comparingInt(EvidencePropertyDomain::getStanza);	
			Comparator<EvidencePropertyDomain> compareBySequenceNum = Comparator.comparingInt(EvidencePropertyDomain::getSequenceNum);			 
			Comparator<EvidencePropertyDomain> compareByTerm = Comparator.comparing(EvidencePropertyDomain::getPropertyTerm);			 
			Comparator<EvidencePropertyDomain> compareAll = compareByStanza.thenComparing(compareBySequenceNum).thenComparing(compareByTerm);
			Collections.sort(domain.getProperties(), compareAll);		
		}

		// notes
		if (entity.getAllNotes() != null && !entity.getAllNotes().isEmpty()) {
			Iterable<NoteDomain> note = noteTranslator.translateEntities(entity.getAllNotes());
			domain.setAllNotes(IteratorUtils.toList(note.iterator()));
		}
		
		return domain;
	}

}
