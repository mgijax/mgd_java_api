package org.jax.mgi.mgd.api.model.bib.translator;

import java.util.ArrayList;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.bib.domain.LTReferenceDomain;
import org.jax.mgi.mgd.api.model.bib.entities.LTReference;
import org.jax.mgi.mgd.api.model.bib.entities.LTReferenceAssociatedData;
import org.jax.mgi.mgd.api.model.bib.entities.LTReferenceBook;
import org.jax.mgi.mgd.api.model.bib.entities.LTReferenceWorkflowData;
import org.jax.mgi.mgd.api.util.Constants;

public class LTReferenceTranslator extends BaseEntityDomainTranslator<LTReference, LTReferenceDomain>{
	@Override
	protected LTReferenceDomain entityToDomain(LTReference entity, int translationDepth) {
		if (entity == null) { return null; }
		LTReferenceDomain domain = new LTReferenceDomain();

		// basic 1-for-1 fields
		domain._refs_key = entity.get_refs_key();
		domain.authors = entity.getAuthors();
		domain.primary_author = entity.getPrimary_author();
		domain.title = entity.getTitle();
		domain.journal = entity.getJournal();
		domain.volume = entity.getVolume();
		domain.issue = entity.getIssue();
		domain.date = entity.getDate();

		if (entity.getYear() != null) {
			domain.year = entity.getYear().toString();
		}
		else {
			domain.year = null;
		}
		
		domain.pages = entity.getPages();
		domain.ref_abstract = entity.getRef_abstract();
		domain.referencenote = entity.getReferencenote();
		domain.jnumid = entity.getJnumid();
		domain.doiid = entity.getDoiid();
		domain.pubmedid = entity.getPubmedid();
		domain.mgiid = entity.getMgiid();
		domain.gorefid = entity.getGorefid();
		domain.reference_type = entity.getReferenceType();
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
		if (entity.getIs_discard() == 0) {
			domain.is_discard = "No";
		} else {
			domain.is_discard = "Yes";
		}
		
		// list of strings, each of which indicates a type of data associated with the reference
		domain.associated_data = new ArrayList<String>();
		LTReferenceAssociatedData flags = entity.getAssociatedData();
		if (flags != null) {
			if (flags.getHas_gxdindex() != 0) { domain.associated_data.add("GXD Index"); }
			if (flags.getHas_gxdimages() != 0) { domain.associated_data.add("GXD/CRE Images"); }
			if (flags.getHas_gxdspecimens() != 0) { domain.associated_data.add("GXD/CRE Specimens"); }
			if (flags.getHas_probes() != 0) { domain.associated_data.add("Probes"); }
			if (flags.getHas_antibodies() != 0) { domain.associated_data.add("Antibodies"); }
			if (flags.getHas_gxdresults() != 0) { domain.associated_data.add("GXD/CRE Results"); }
			if (flags.getHas_gxdresults() != 0) { domain.associated_data.add("GXD/CRE Assays"); }
			if (flags.getHas_alleles() != 0) { domain.associated_data.add("Alleles"); }
			if (flags.getHas_markers() != 0) { domain.associated_data.add("Markers"); }
		}
		
		// fields only applicable for references of type "book"
		LTReferenceBook bookData = entity.getBookData();
		if (bookData != null) {
			domain.book_author = bookData.getBook_author();
			domain.book_title = bookData.getBook_title();
			domain.place = bookData.getPlace();
			domain.publisher = bookData.getPublisher();
			domain.series_edition = bookData.getSeries_edition();
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
		return domain;
	}

	@Override
	protected LTReference domainToEntity(LTReferenceDomain domain, int translationDepth) {
		// Cannot do translation here, as it requires lookup of actual entity from database.  Must
		// instead work with ReferenceRepository.
		
		return null;
	}
}
