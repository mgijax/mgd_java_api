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
import org.jax.mgi.mgd.api.model.bib.domain.ReferenceWorkflowDataDomain;
import org.jax.mgi.mgd.api.model.bib.domain.ReferenceWorkflowRelevanceDomain;
import org.jax.mgi.mgd.api.model.bib.domain.ReferenceWorkflowStatusDomain;
import org.jax.mgi.mgd.api.model.bib.domain.ReferenceWorkflowTagDomain;
import org.jax.mgi.mgd.api.model.bib.entities.Reference;
import org.jax.mgi.mgd.api.util.DecodeString;
import org.jboss.logging.Logger;

public class ReferenceTranslator extends BaseEntityDomainTranslator<Reference, ReferenceDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	private AccessionTranslator accessionTranslator = new AccessionTranslator();
	private ReferenceNoteTranslator noteTranslator = new ReferenceNoteTranslator();
	private ReferenceWorkflowDataTranslator wfDataTranslator = new ReferenceWorkflowDataTranslator();
	private ReferenceWorkflowRelevanceTranslator wfRelevanceTranslator = new ReferenceWorkflowRelevanceTranslator();
	private ReferenceWorkflowStatusTranslator wfStatusTranslator = new ReferenceWorkflowStatusTranslator();
	private ReferenceWorkflowTagTranslator wfTagTranslator = new ReferenceWorkflowTagTranslator();

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
			List<ReferenceBookDomain> bookList = IteratorUtils.toList(book.iterator());
			domain.setBook_author(bookList.get(0).getBook_author());
			domain.setBook_title(bookList.get(0).getBook_title());
			domain.setPlace(bookList.get(0).getPlace());
			domain.setPublisher(bookList.get(0).getPublisher());
			domain.setSeries_ed(bookList.get(0).getSeries_ed());			
		}
		
		// reference book
//		if (entity.getReferenceBook() != null && !entity.getReferenceBook().isEmpty()) {
//			Iterable<ReferenceBookDomain> book = bookTranslator.translateEntities(entity.getReferenceBook());
//			domain.setReferenceBook(book.iterator().next());		
//		}
		
		// reference note
		if (entity.getReferenceNote() != null && !entity.getReferenceNote().isEmpty()) {
			Iterable<ReferenceNoteDomain> note = noteTranslator.translateEntities(entity.getReferenceNote());
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
	
		// workflow data
		if (entity.getWorkflowData() != null && entity.getWorkflowData().isEmpty()) {
			Iterable<ReferenceWorkflowDataDomain> wfData = wfDataTranslator.translateEntities(entity.getWorkflowData());
			domain.setWorkflowData(IteratorUtils.toList(wfData.iterator()));			
		}
		
		// workflow relevance
		if (entity.getWorkflowRelevance() != null && entity.getWorkflowRelevance().isEmpty()) {
			Iterable<ReferenceWorkflowRelevanceDomain> wfRelevance = wfRelevanceTranslator.translateEntities(entity.getWorkflowRelevance());
			domain.setWorkflowRelevance(IteratorUtils.toList(wfRelevance.iterator()));			
		}
		
		// workflow status
		if (entity.getWorkflowStatus() != null && entity.getWorkflowStatus().isEmpty()) {
			Iterable<ReferenceWorkflowStatusDomain> wfStatus = wfStatusTranslator.translateEntities(entity.getWorkflowStatus());
			domain.setWorkflowStatus(IteratorUtils.toList(wfStatus.iterator()));			
		}
		
		// workflow tag
		if (entity.getWorkflowTag() != null && entity.getWorkflowTag().isEmpty()) {
			Iterable<ReferenceWorkflowTagDomain> wfTag = wfTagTranslator.translateEntities(entity.getWorkflowTag());
			domain.setWorkflowTag(IteratorUtils.toList(wfTag.iterator()));			
		}
		
		return domain;
	}

}
