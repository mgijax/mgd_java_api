package org.jax.mgi.mgd.api.model.bib.translator;

import java.util.ArrayList;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.bib.domain.LTReferenceDomain;
import org.jax.mgi.mgd.api.model.bib.domain.ReferenceBookDomain;
import org.jax.mgi.mgd.api.model.bib.domain.ReferenceNoteDomain;
import org.jax.mgi.mgd.api.model.bib.domain.ReferenceWorkflowDataDomain;
import org.jax.mgi.mgd.api.model.bib.domain.ReferenceWorkflowRelevanceDomain;
import org.jax.mgi.mgd.api.model.bib.domain.ReferenceWorkflowStatusDomain;
import org.jax.mgi.mgd.api.model.bib.entities.LTReference;
import org.jax.mgi.mgd.api.model.bib.entities.ReferenceAssociatedData;
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
		domain.primary_author = entity.getPrimaryAuthor();
		domain.title = entity.getTitle();
		domain.journal = entity.getJournal();
		domain.vol = entity.getVol();
		domain.issue = entity.getIssue();
		domain.pgs = entity.getPgs();
		domain.date = entity.getDate();
		domain.isReviewArticle = String.valueOf(entity.getIsReviewArticle());

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
		
		domain.referenceType = entity.getReferenceTypeTerm().getTerm();
		domain.referenceTypeKey = String.valueOf(entity.getReferenceTypeTerm().get_term_key());
		domain.short_citation = entity.getCitationData().get(0).getShort_citation();
		
		domain.ap_status = entity.getStatus(Constants.WG_AP);
		domain.go_status = entity.getStatus(Constants.WG_GO);
		domain.gxd_status = entity.getStatus(Constants.WG_GXD);
		domain.pro_status = entity.getStatus(Constants.WG_PRO);
		domain.qtl_status = entity.getStatus(Constants.WG_QTL);
		domain.tumor_status = entity.getStatus(Constants.WG_TUMOR);
		
		domain.workflow_tags = entity.getWorkflowTagsAsStrings();
		
		domain.creation_date = dateFormatter.format(entity.getCreation_date());
		domain.modification_date = dateFormatter.format(entity.getModification_date());
		domain.createdBy = entity.getCreatedBy().getLogin();
		domain.modifiedBy = entity.getModifiedBy().getLogin();
		
		// list of strings, each of which indicates a type of data associated with the reference
		domain.associated_data = new ArrayList<String>();
		ReferenceAssociatedData flags = entity.getAssociatedData();
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
		if (entity.getReferenceNote() != null && !entity.getReferenceNote().isEmpty()) {
			ReferenceNoteTranslator noteTranslator = new ReferenceNoteTranslator();
			Iterable<ReferenceNoteDomain> note = noteTranslator.translateEntities(entity.getReferenceNote());
			domain.setReferenceNote(note.iterator().next());
		}
		
		// at most one reference book
		if (entity.getReferenceBook() != null && !entity.getReferenceBook().isEmpty()) {
			ReferenceBookTranslator bookTranslator = new ReferenceBookTranslator();
			Iterable<ReferenceBookDomain> book = bookTranslator.translateEntities(entity.getReferenceBook());
			domain.setReferenceBook(book.iterator().next());			
		}

		// data specific to workflows: has supplemental data?, link to supplemental data, has PDF?, has extracted text?
		// only grabs the extracted text "body" (done in the entity)
		if (entity.getWorkflowData() != null) {
			ReferenceWorkflowDataTranslator dataTranslator = new ReferenceWorkflowDataTranslator();
			Iterable<ReferenceWorkflowDataDomain> i = dataTranslator.translateEntities(entity.getWorkflowData());
			domain.setWorkflowData(i.iterator().next());	
		}

		// bib_workflow_status
		if (entity.getWorkflowStatus() != null) {
			ReferenceWorkflowStatusTranslator statusTranslator = new ReferenceWorkflowStatusTranslator();
			Iterable<ReferenceWorkflowStatusDomain> i = statusTranslator.translateEntities(entity.getWorkflowStatus());
			domain.setStatusHistory(IteratorUtils.toList(i.iterator()));
		}
		
		// bib_workflow_relevance
		if (entity.getWorkflowRelevance() != null) {
			ReferenceWorkflowRelevanceTranslator relevanceTranslator = new ReferenceWorkflowRelevanceTranslator();
			Iterable<ReferenceWorkflowRelevanceDomain> i = relevanceTranslator.translateEntities(entity.getWorkflowRelevance());
			domain.setRelevanceHistory(IteratorUtils.toList(i.iterator()));
			domain.setEditRelevance(domain.getRelevanceHistory().get(0).getRelevance());
			domain.setEditRelevanceKey(domain.getRelevanceHistory().get(0).getRelevanceKey());			
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
