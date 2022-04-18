package org.jax.mgi.mgd.api.model.bib.translator;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.acc.domain.AccessionDomain;
import org.jax.mgi.mgd.api.model.acc.translator.AccessionTranslator;
import org.jax.mgi.mgd.api.model.bib.domain.ReferenceBookDomain;
import org.jax.mgi.mgd.api.model.bib.domain.ReferenceDomain;
import org.jax.mgi.mgd.api.model.bib.domain.ReferenceNoteDomain;
import org.jax.mgi.mgd.api.model.bib.entities.Reference;
import org.jax.mgi.mgd.api.util.DecodeString;
import org.jboss.logging.Logger;

public class ReferenceTranslator extends BaseEntityDomainTranslator<Reference, ReferenceDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	private AccessionTranslator accessionTranslator = new AccessionTranslator();
	private ReferenceNoteTranslator noteTranslator = new ReferenceNoteTranslator();

	@Override
	protected ReferenceDomain entityToDomain(Reference entity) {

		ReferenceDomain domain = new ReferenceDomain();
		
		domain.setRefsKey(String.valueOf(entity.get_refs_key()));
		domain.setPrimaryAuthor(entity.getPrimaryAuthor());
		domain.setAuthors(entity.getAuthors());
		domain.setTitle(entity.getTitle());
		domain.setJournal(entity.getJournal());
		domain.setVol(entity.getVol());
		domain.setIssue(entity.getIssue());
		domain.setDate(entity.getDate());
		domain.setYear(String.valueOf(entity.getYear()));
		domain.setPgs(entity.getPgs());
		domain.setReferenceAbstract(DecodeString.getDecodeToUTF8(entity.getReferenceAbstract()));
		domain.setDate(entity.getDate());
		domain.setIsReviewArticle(String.valueOf(entity.getIsReviewArticle()));
		domain.setReferenceTypeKey(String.valueOf(entity.getReferenceType().get_term_key()));
		domain.setReferenceType(entity.getReferenceType().getTerm());
		
		if (entity.getReferenceCitationCache() != null) {
			domain.setJnumid(entity.getReferenceCitationCache().getJnumid());
			domain.setJnum(String.valueOf(entity.getReferenceCitationCache().getNumericPart()));		
			domain.setShort_citation(entity.getReferenceCitationCache().getShort_citation());
		}
		
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));

		// ReferenceDomain must agree with what is coming from PWI/LitTriage
		
		// reference book
		if (entity.getReferenceBook() != null && !entity.getReferenceBook().isEmpty()) {
			ReferenceBookTranslator bookTranslator = new ReferenceBookTranslator();
			Iterable<ReferenceBookDomain> book = bookTranslator.translateEntities(entity.getReferenceBook());
			domain.setReferenceBook(book.iterator().next());		
		}
		
		// reference note
		if (entity.getReferenceNote() != null && !entity.getReferenceNote().isEmpty()) {
			Iterable<ReferenceNoteDomain> note = noteTranslator.translateEntities(entity.getReferenceNote());
			//domain.setReferenceNote(note.iterator().next());
			domain.setReferenceNote(note.iterator().next().getNote());
		}
		
		// first mgi accession id only
		if (entity.getMgiAccessionIds() != null && !entity.getMgiAccessionIds().isEmpty()) {
			Iterable<AccessionDomain> acc = accessionTranslator.translateEntities(entity.getMgiAccessionIds());
			//domain.setMgiAccessionIds(IteratorUtils.toList(acc.iterator()));
			domain.setMgiid(acc.iterator().next().getAccID());		
		}

		// non-mgi accession ids
		if (entity.getEditAccessionIds() != null && !entity.getEditAccessionIds().isEmpty()) {
			Iterable<AccessionDomain> acc = accessionTranslator.translateEntities(entity.getEditAccessionIds());
			List<AccessionDomain> editAccessionIds = new ArrayList<AccessionDomain>();
			editAccessionIds.addAll(IteratorUtils.toList(acc.iterator()));
			for (int i = 0; i < editAccessionIds.size(); i++) {
				if (editAccessionIds.get(i).getLogicaldbKey().equals("29")) {
					domain.setPubmedid(editAccessionIds.get(i).getAccID());
				}
				else if (editAccessionIds.get(i).getLogicaldbKey().equals("65")) {
					domain.setDoiid(editAccessionIds.get(i).getAccID());
				}				
				else if (editAccessionIds.get(i).getLogicaldbKey().equals("185")) {
					domain.setGorefid(editAccessionIds.get(i).getAccID());
				}				
			}
			//domain.getEditAccessionIds(IteratorUtils.toList(acc.iterator()));
			//domain.getEditAccessionIds().sort(Comparator.comparing(AccessionDomain::getLogicaldb).thenComparing(AccessionDomain::getAccID));
		}
	
		return domain;
	}

}
