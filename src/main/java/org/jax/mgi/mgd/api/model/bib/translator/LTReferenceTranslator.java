package org.jax.mgi.mgd.api.model.bib.translator;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.bib.domain.LTReferenceDomain;
import org.jax.mgi.mgd.api.model.bib.domain.ReferenceBookDomain;
import org.jax.mgi.mgd.api.model.bib.domain.ReferenceNoteDomain;
import org.jax.mgi.mgd.api.model.bib.domain.ReferenceWorkflowDataDomain;
import org.jax.mgi.mgd.api.model.bib.domain.ReferenceWorkflowRelevanceDomain;
import org.jax.mgi.mgd.api.model.bib.domain.ReferenceWorkflowStatusDomain;
import org.jax.mgi.mgd.api.model.bib.domain.ReferenceWorkflowTagDomain;
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
		domain.primaryAuthor = entity.getPrimaryAuthor();
		domain.authors = entity.getAuthors();		
		domain.title = entity.getTitle();
		domain.journal = entity.getJournal();
		domain.vol = entity.getVol();
		domain.issue = entity.getIssue();
		domain.pgs = entity.getPgs();
		domain.date = entity.getDate();
		domain.setYear(String.valueOf(entity.getYear()));		
		domain.isReviewArticle = String.valueOf(entity.getIsReviewArticle());
		domain.referenceAbstract = DecodeString.getDecodeToUTF8(entity.getReferenceAbstract());
		domain.referenceType = entity.getReferenceTypeTerm().getTerm();
		domain.referenceTypeKey = String.valueOf(entity.getReferenceTypeTerm().get_term_key());
		domain.creation_date = dateFormatter.format(entity.getCreation_date());
		domain.modification_date = dateFormatter.format(entity.getModification_date());
		domain.createdBy = entity.getCreatedBy().getLogin();
		domain.modifiedBy = entity.getModifiedBy().getLogin();
			
		if (entity.getReferenceCitationCache() != null) {
			domain.setJnumid(entity.getReferenceCitationCache().getJnumid());
			domain.setJnum(String.valueOf(entity.getReferenceCitationCache().getNumericPart()));		
			domain.setShort_citation(entity.getReferenceCitationCache().getShort_citation());
		}
		
		domain.mgiid = entity.getMgiid();
		domain.doiid = entity.getDoiid();
		domain.pubmedid = entity.getPubmedid();
		domain.gorefid = entity.getGorefid();
		
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
		
		// bib_workflow_tag
		if (entity.getWorkflowTags() != null) {
			ReferenceWorkflowTagTranslator tagTranslator = new ReferenceWorkflowTagTranslator();
			Iterable<ReferenceWorkflowTagDomain> i = tagTranslator.translateEntities(entity.getWorkflowTags());
			domain.setWorkflowTags(IteratorUtils.toList(i.iterator()));
			List<String> workflowTagString = new ArrayList<>();
			for (int t = 0; t < domain.getWorkflowTags().size(); t++) {
				workflowTagString.add(domain.getWorkflowTags().get(t).getTag());
			}
			domain.setWorkflowTagString(workflowTagString);
		}		
		
		// bib_workflow_status
		if (entity.getWorkflowStatus() != null) {
			ReferenceWorkflowStatusTranslator statusTranslator = new ReferenceWorkflowStatusTranslator();
			Iterable<ReferenceWorkflowStatusDomain> i = statusTranslator.translateEntities(entity.getWorkflowStatus());
			domain.setStatusHistory(IteratorUtils.toList(i.iterator()));
		}

		// bib_workflow_status where isCurrent = 1
		if (entity.getWorkflowStatusCurrent() != null) {
			ReferenceWorkflowStatusTranslator statusTranslator = new ReferenceWorkflowStatusTranslator();
			Iterable<ReferenceWorkflowStatusDomain> i = statusTranslator.translateEntities(entity.getWorkflowStatusCurrent());
			domain.setStatusCurrent(IteratorUtils.toList(i.iterator()));

			for (int s = 0; s < domain.getStatusCurrent().size(); s++) {
				if (domain.getStatusCurrent().get(s).getGroupAbbrev().equals(Constants.WG_AP)) {
					domain.setAp_status(domain.getStatusCurrent().get(s).getStatus());
					//domain.setAp_statusKey(domain.getStatusCurrent().get(i).getStatusKey());
				}
				else if (domain.getStatusCurrent().get(s).getGroupAbbrev().equals(Constants.WG_GO)) {
					domain.setGo_status(domain.getStatusCurrent().get(s).getStatus());
					//domain.setGo_statusKey(domain.getStatusCurrent().get(i).getStatusKey());
				}	
				else if (domain.getStatusCurrent().get(s).getGroupAbbrev().equals(Constants.WG_GXD)) {
					domain.setGxd_status(domain.getStatusCurrent().get(s).getStatus());
					//domain.setGxd_statusKey(domain.getStatusCurrent().get(i).getStatusKey());
				}
				else if (domain.getStatusCurrent().get(s).getGroupAbbrev().equals(Constants.WG_PRO)) {
					domain.setPro_status(domain.getStatusCurrent().get(s).getStatus());
					//domain.setPro_statusKey(domain.getStatusCurrent().get(i).getStatusKey());
				}	
				else if (domain.getStatusCurrent().get(s).getGroupAbbrev().equals(Constants.WG_QTL)) {
					domain.setQtl_status(domain.getStatusCurrent().get(s).getStatus());
					//domain.setQtl_statusKey(domain.getStatusCurrent().get(i).getStatusKey());
				}	
				else if (domain.getStatusCurrent().get(s).getGroupAbbrev().equals(Constants.WG_TUMOR)) {
					domain.setTumor_status(domain.getStatusCurrent().get(s).getStatus());
					//domain.setTumor_statusKey(domain.getStatusCurrent().get(i).getStatusKey());
				}			
			}
		}
		
		// bib_workflow_relevance
		if (entity.getWorkflowRelevance() != null) {
			ReferenceWorkflowRelevanceTranslator relevanceTranslator = new ReferenceWorkflowRelevanceTranslator();
			Iterable<ReferenceWorkflowRelevanceDomain> i = relevanceTranslator.translateEntities(entity.getWorkflowRelevance());
			domain.setRelevanceHistory(IteratorUtils.toList(i.iterator()));
			domain.setEditRelevance(domain.getRelevanceHistory().get(0).getRelevance());
			domain.setEditRelevanceKey(domain.getRelevanceHistory().get(0).getRelevanceKey());			
		}

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
