package org.jax.mgi.mgd.api.model.bib.translator;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.bib.domain.LTReferenceDomain;
import org.jax.mgi.mgd.api.model.bib.domain.ReferenceBookDomain;
import org.jax.mgi.mgd.api.model.bib.domain.ReferenceNoteDomain;
import org.jax.mgi.mgd.api.model.bib.entities.LTReference;
import org.jax.mgi.mgd.api.model.bib.entities.LTReferenceAssociatedData;
import org.jax.mgi.mgd.api.model.bib.entities.LTReferenceWorkflowData;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.DecodeString;

public class LTReferenceTranslator extends BaseEntityDomainTranslator<LTReference, LTReferenceDomain>{
	@Override
	protected LTReferenceDomain entityToDomain(LTReference entity) {
		
		if (entity == null) { return null; }
		
		LTReferenceDomain domain = new LTReferenceDomain();

		// basic 1-for-1 fields
		domain.refsKey = String.valueOf(entity.get_refs_key());
		domain.authors = entity.getAuthors();
		domain.primary_author = entity.getPrimary_author();
		domain.title = entity.getTitle();
		domain.journal = entity.getJournal();
		domain.vol = entity.getVol();
		domain.issue = entity.getIssue();
		domain.pgs = entity.getPgs();
		domain.date = entity.getDate();

		if (entity.getYear() != null) {
			domain.year = entity.getYear().toString();
		}
		else {
			domain.year = null;
		}
		
		domain.referenceAbstract = DecodeString.getDecodeToUTF8(entity.getReferenceAbstract());
		domain.mgiid = entity.getMgiid();
		domain.jnumid = entity.getJnumid();
		domain.doiid = entity.getDoiid();
		domain.pubmedid = entity.getPubmedid();
		domain.gorefid = entity.getGorefid();	
		
		domain.referenceType = entity.getReferenceType();
		domain.referenceTypeKey = String.valueOf(entity.getReferenceTypeTerm().get_term_key());
		domain.short_citation = entity.getShort_citation();
		
		domain.ap_status = entity.getStatus(Constants.WG_AP);
		domain.go_status = entity.getStatus(Constants.WG_GO);
		domain.gxd_status = entity.getStatus(Constants.WG_GXD);
		domain.qtl_status = entity.getStatus(Constants.WG_QTL);
		domain.tumor_status = entity.getStatus(Constants.WG_TUMOR);
		domain.workflow_tags = entity.getWorkflowTagsAsStrings();
		
		domain.creation_date = dateFormatter.format(entity.getCreation_date());
		domain.modification_date = dateFormatter.format(entity.getModification_date());
		domain.created_by = entity.getCreatedByUser().getLogin();
		domain.modified_by = entity.getModifiedByUser().getLogin();
		
		// is this a review article?
		if (entity.getIsReviewArticle() == 0) {
			domain.isReviewArticle = "No";
		} else {
			domain.isReviewArticle = "Yes";
		}

		// has this article been discarded?
//		if (entity.getIsDiscard() == 0) {
//			domain.isDiscard = "No";
//		} else {
//			domain.isDiscard = "Yes";
//		}
		
		// list of strings, each of which indicates a type of data associated with the reference
		domain.associated_data = new ArrayList<String>();
		LTReferenceAssociatedData flags = entity.getAssociatedData();
		if (flags != null) {
			if (flags.getHas_alleles() != 0) { domain.associated_data.add("Alleles"); }
			if (flags.getHas_antibodies() != 0) { domain.associated_data.add("Antibodies"); }
			if (flags.getHas_go() != 0) { domain.associated_data.add("GO"); }
			if (flags.getHas_gxdindex() != 0) { domain.associated_data.add("GXD Index"); }
			if (flags.getHas_gxdimages() != 0) { domain.associated_data.add("GXD/CRE Images"); }
			if (flags.getHas_gxdspecimens() != 0) { domain.associated_data.add("GXD/CRE Specimens"); }
			if (flags.getHas_gxdresults() != 0) { domain.associated_data.add("GXD/CRE Assays"); }
			if (flags.getHas_gxdresults() != 0) { domain.associated_data.add("GXD/CRE Results"); }
			if (flags.getHas_mapping() != 0) { domain.associated_data.add("Mapping"); }
			if (flags.getHas_markers() != 0) { domain.associated_data.add("Markers"); }
			if (flags.getHas_probes() != 0) { domain.associated_data.add("Probes"); }
			if (flags.getHas_strain() != 0) { domain.associated_data.add("Strain"); }			
		}
			
		// at most one reference note
		if (entity.getNotes() != null && !entity.getNotes().isEmpty()) {
			ReferenceNoteTranslator noteTranslator = new ReferenceNoteTranslator();
			Iterable<ReferenceNoteDomain> note = noteTranslator.translateEntities(entity.getNotes());
			List<ReferenceNoteDomain> noteList = IteratorUtils.toList(note.iterator());			
			domain.referenceNote = noteList.get(0).getNote();
		}
		
		// at most one reference book
		if (entity.getReferenceBook() != null && !entity.getReferenceBook().isEmpty()) {
			ReferenceBookTranslator bookTranslator = new ReferenceBookTranslator();
			Iterable<ReferenceBookDomain> book = bookTranslator.translateEntities(entity.getReferenceBook());
			List<ReferenceBookDomain> bookList = IteratorUtils.toList(book.iterator());
			domain.book_author = bookList.get(0).getBook_author();
			domain.book_title = bookList.get(0).getBook_title();
			domain.place = bookList.get(0).getPlace();
			domain.publisher = bookList.get(0).getPublisher();
			domain.series_ed = bookList.get(0).getSeries_ed();			
		}

		// data specific to workflows: has supplemental data?, link to supplemental data, has PDF?, has extracted text?
		LTReferenceWorkflowData workflowData = entity.getWorkflowData();
		if (workflowData != null) {
			domain.has_supplemental = workflowData.getSupplemental();
			domain.link_to_supplemental = workflowData.getLink_supplemental();
			if (workflowData.getHas_pdf() == 0) {
				domain.has_pdf = "No";
			} else {
				domain.has_pdf = "Yes";
			}
			if ((workflowData.getExtracted_text() != null) && (workflowData.getExtracted_text().length() > 0)) {
				domain.has_extracted_text = "Yes";
			} else {
				domain.has_extracted_text = "No";
			}
		}

		// turning this on causes a LazyINitializationExpception; no idea why
		// one-to-many allele associations
//		if (entity.getAlleleAssocs() != null && !entity.getAlleleAssocs().isEmpty()) {
//			MGIReferenceAssocTranslator assocTranslator = new MGIReferenceAssocTranslator();
//			Iterable<MGIReferenceAssocDomain> i = assocTranslator.translateEntities(entity.getAlleleAssocs());
//			domain.setAlleleAssocs(IteratorUtils.toList(i.iterator()));
//		}
		
		return domain;
	}

}
