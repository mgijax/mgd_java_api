package org.jax.mgi.mgd.api.model.voc.translator;

import java.util.Comparator;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.acc.domain.AccessionDomain;
import org.jax.mgi.mgd.api.model.acc.translator.AccessionTranslator;
import org.jax.mgi.mgd.api.model.mgi.domain.MGISynonymDomain;
import org.jax.mgi.mgd.api.model.mgi.translator.MGISynonymTranslator;
import org.jax.mgi.mgd.api.model.voc.domain.TermDomain;
import org.jax.mgi.mgd.api.model.voc.entities.Term;
import org.jax.mgi.mgd.api.util.Constants;
import org.jboss.logging.Logger;

public class TermTranslator extends BaseEntityDomainTranslator<Term, TermDomain> {
	
	protected Logger log = Logger.getLogger(getClass());

	private AccessionTranslator accessionTranslator = new AccessionTranslator();
	private MGISynonymTranslator synonymTranslator = new MGISynonymTranslator();
	
	@Override
	protected TermDomain entityToDomain(Term entity) {
		TermDomain domain = new TermDomain();
		
		log.info("vocab key:" + entity.get_term_key());
		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
		domain.setTermKey(String.valueOf(entity.get_term_key()));
		domain.setVocabKey(String.valueOf(entity.get_vocab_key()));
		domain.setTerm(entity.getTerm());
		domain.setAbbreviation(entity.getAbbreviation());
		domain.setNote(entity.getNote());
		domain.setSequenceNum(String.valueOf(entity.getSequenceNum()));
		domain.setIsObsolete(String.valueOf(entity.getIsObsolete()));
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
		
		// set hasDAG (select distinct v.* from VOC_VocabDAG d, VOC_Vocab v where d._vocab_key = v._vocab_key)
		if (entity.getVocab() != null) {
			domain.setVocabName(entity.getVocab().getName());

			if (entity.getVocab().getVocabDAG() != null && !entity.getVocab().getVocabDAG().isEmpty()) {
				domain.setHasDAG(true);
			}
			else {
				domain.setHasDAG(false);
			}
		}
		else {
			domain.setHasDAG(false);
		}
		
		if (entity.getAccessionIds() != null && !entity.getAccessionIds().isEmpty()) {
			Iterable<AccessionDomain> acc = accessionTranslator.translateEntities(entity.getAccessionIds());		
			if (acc.iterator().hasNext() == true) {			
				domain.setAccessionIds(IteratorUtils.toList(acc.iterator()));
			}
		}
		
		// one-to-many secondary accession ids
		if (entity.getAccessionSecondaryIds() != null && !entity.getAccessionSecondaryIds().isEmpty()) {
			Iterable<AccessionDomain> acc = accessionTranslator.translateEntities(entity.getAccessionSecondaryIds());		
			domain.setAccessionSecondaryIds(IteratorUtils.toList(acc.iterator()));
		}
		
		// GO-DAG-abbreviation
		if (entity.getGoDagNodes() != null && !entity.getGoDagNodes().isEmpty()) {
			domain.setGoDagAbbrev(entity.getGoDagNodes().get(0).getDag().getAbbreviation().trim());
		}
		
        // one-to-many term synonyms
	
	    if (entity.getSynonyms() != null && !entity.getSynonyms().isEmpty()) {
            Iterable<MGISynonymDomain> i = synonymTranslator.translateEntities(entity.getSynonyms());
            domain.setSynonyms(IteratorUtils.toList(i.iterator()));
            domain.getSynonyms().sort(Comparator.comparing(MGISynonymDomain::getSynonymTypeKey).thenComparing(MGISynonymDomain::getSynonym, String.CASE_INSENSITIVE_ORDER));
        }
        if (entity.getGoRelSynonyms() != null && !entity.getGoRelSynonyms().isEmpty()) {
            Iterable<MGISynonymDomain> i = synonymTranslator.translateEntities(entity.getGoRelSynonyms());
            domain.setGoRelSynonyms(IteratorUtils.toList(i.iterator()));
            domain.getGoRelSynonyms().sort(Comparator.comparing(MGISynonymDomain::getSynonymTypeKey).thenComparing(MGISynonymDomain::getSynonym, String.CASE_INSENSITIVE_ORDER));
        }
        if (entity.getExactSynonyms() != null && !entity.getExactSynonyms().isEmpty()) {
    	   	Iterable<MGISynonymDomain> i = synonymTranslator.translateEntities(entity.getExactSynonyms());
    	   	domain.setExactSynonyms(IteratorUtils.toList(i.iterator()));
    	   	domain.getExactSynonyms().sort(Comparator.comparing(MGISynonymDomain::getSynonymTypeKey).thenComparing(MGISynonymDomain::getSynonym, String.CASE_INSENSITIVE_ORDER));
        }
              
        return domain;
	}

}
