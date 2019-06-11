package org.jax.mgi.mgd.api.model.bib.translator;

import java.util.Comparator;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.acc.domain.AccessionDomain;
import org.jax.mgi.mgd.api.model.acc.translator.AccessionTranslator;
import org.jax.mgi.mgd.api.model.bib.domain.ReferenceBookDomain;
import org.jax.mgi.mgd.api.model.bib.domain.ReferenceDomain;
import org.jax.mgi.mgd.api.model.bib.domain.ReferenceNoteDomain;
import org.jax.mgi.mgd.api.model.bib.entities.Reference;
import org.jax.mgi.mgd.api.model.mgi.domain.MGIReferenceAssocDomain;
import org.jax.mgi.mgd.api.model.mgi.translator.MGIReferenceAssocTranslator;
import org.jboss.logging.Logger;

public class ReferenceTranslator extends BaseEntityDomainTranslator<Reference, ReferenceDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	private AccessionTranslator accessionTranslator = new AccessionTranslator();
	private ReferenceBookTranslator bookTranslator = new ReferenceBookTranslator();
	private ReferenceNoteTranslator noteTranslator = new ReferenceNoteTranslator();
	private MGIReferenceAssocTranslator assocTranslator = new MGIReferenceAssocTranslator();

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
		domain.setReferenceAbstract(entity.getReferenceAbstract());
		domain.setDate(entity.getDate());
		domain.setIsReviewArticle(String.valueOf(entity.getIsReviewArticle()));
		domain.setIsDiscard(String.valueOf(entity.getIsDiscard()));
		domain.setReferenceTypeKey(String.valueOf(entity.getReferenceType().get_term_key()));
		domain.setReferenceType(entity.getReferenceType().getTerm());
		domain.setJnumid(entity.getReferenceCitationCache().getJnumid());
		domain.setJnum(String.valueOf(entity.getReferenceCitationCache().getNumericPart()));		
		domain.setShort_citation(entity.getReferenceCitationCache().getShort_citation());
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));

		// reference book
		if (entity.getReferenceBook() != null && !entity.getReferenceBook().isEmpty()) {
			Iterable<ReferenceBookDomain> book = bookTranslator.translateEntities(entity.getReferenceBook());
			domain.setReferenceBook(book.iterator().next());		
		}
		
		// reference note
		if (entity.getReferenceNote() != null && !entity.getReferenceNote().isEmpty()) {
			Iterable<ReferenceNoteDomain> note = noteTranslator.translateEntities(entity.getReferenceNote());
			domain.setReferenceNote(note.iterator().next());
		}
		
		// mgi accession ids only
		if (entity.getMgiAccessionIds() != null && !entity.getMgiAccessionIds().isEmpty()) {
			Iterable<AccessionDomain> acc = accessionTranslator.translateEntities(entity.getMgiAccessionIds());
			domain.setMgiAccessionIds(IteratorUtils.toList(acc.iterator()));
		}

		// accession ids editable
		if (entity.getEditAccessionIds() != null && !entity.getEditAccessionIds().isEmpty()) {
			Iterable<AccessionDomain> acc = accessionTranslator.translateEntities(entity.getEditAccessionIds());
			domain.setEditAccessionIds(IteratorUtils.toList(acc.iterator()));
			domain.getEditAccessionIds().sort(Comparator.comparing(AccessionDomain::getLogicaldb).thenComparing(AccessionDomain::getAccID));
		}
	
		// one-to-many associations
		//if (entity.getRefAssocs() != null && !entity.getRefAssocs().isEmpty()) {
		//	Iterable<MGIReferenceAssocDomain> i = assocTranslator.translateEntities(entity.getRefAssocs());
		//	domain.setRefAssocs(IteratorUtils.toList(i.iterator()));
		//}
		
		return domain;
	}

}
