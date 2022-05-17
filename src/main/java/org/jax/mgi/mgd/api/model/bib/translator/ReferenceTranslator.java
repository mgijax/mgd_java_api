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
import org.jax.mgi.mgd.api.model.bib.entities.ReferenceAssociatedData;
import org.jax.mgi.mgd.api.util.Constants;
import org.jax.mgi.mgd.api.util.DecodeString;
import org.jboss.logging.Logger;

public class ReferenceTranslator extends BaseEntityDomainTranslator<Reference, ReferenceDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	private AccessionTranslator accessionTranslator = new AccessionTranslator();

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
		domain.setReferenceTypeKey(String.valueOf(entity.getReferenceTypeTerm().get_term_key()));
		domain.setReferenceType(entity.getReferenceTypeTerm().getTerm());		
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());
		domain.setCreation_date(dateFormatter.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatter.format(entity.getModification_date()));

		// first mgi accession id only
		if (entity.getMgiAccessionIds() != null && !entity.getMgiAccessionIds().isEmpty()) {
			Iterable<AccessionDomain> acc = accessionTranslator.translateEntities(entity.getMgiAccessionIds());
			domain.setMgiid(acc.iterator().next().getAccID());		
		}
		
		// jnum info
		if (entity.getReferenceCitationCache() != null) {
			domain.setJnumid(entity.getReferenceCitationCache().getJnumid());
			domain.setJnum(String.valueOf(entity.getReferenceCitationCache().getNumericPart()));		
			domain.setShort_citation(entity.getReferenceCitationCache().getShort_citation());
		}
					
		// non-mgi accession ids that will be edited
		if (entity.getEditAccessionIds() != null && !entity.getEditAccessionIds().isEmpty()) {
			Iterable<AccessionDomain> acc = accessionTranslator.translateEntities(entity.getEditAccessionIds());			
			List<AccessionDomain> editAccessionIds = new ArrayList<AccessionDomain>();
			editAccessionIds.addAll(IteratorUtils.toList(acc.iterator()));
			for (int i = 0; i < editAccessionIds.size(); i++) {
				if (editAccessionIds.get(i).getLogicaldbKey().equals("1") && editAccessionIds.get(i).getPrefixPart().equals("J:")) {
					// other jnum info is handled above
					domain.setJnumidEdit(editAccessionIds.get(i));
				}
				else if (editAccessionIds.get(i).getLogicaldbKey().equals("65")) {
					domain.setDoiid(editAccessionIds.get(i).getAccID());
					domain.setDoiidEdit(editAccessionIds.get(i));	
				}	
				else if (editAccessionIds.get(i).getLogicaldbKey().equals("29")) {
					domain.setPubmedid(editAccessionIds.get(i).getAccID());
					domain.setPubmedidEdit(editAccessionIds.get(i));
				}				
				else if (editAccessionIds.get(i).getLogicaldbKey().equals("185")) {
					domain.setGorefid(editAccessionIds.get(i).getAccID());
					domain.setGorefidEdit(editAccessionIds.get(i));
				}				
			}
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

		// data specific to workflows: has supplemental data, link to supplemental data, has PDF, has extracted text
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
					domain.setAp_statusKey(domain.getStatusCurrent().get(s).getStatusKey());
				}
				else if (domain.getStatusCurrent().get(s).getGroupAbbrev().equals(Constants.WG_GO)) {
					domain.setGo_status(domain.getStatusCurrent().get(s).getStatus());
					domain.setGo_statusKey(domain.getStatusCurrent().get(s).getStatusKey());
				}	
				else if (domain.getStatusCurrent().get(s).getGroupAbbrev().equals(Constants.WG_GXD)) {
					domain.setGxd_status(domain.getStatusCurrent().get(s).getStatus());
					domain.setGxd_statusKey(domain.getStatusCurrent().get(s).getStatusKey());
				}
				else if (domain.getStatusCurrent().get(s).getGroupAbbrev().equals(Constants.WG_PRO)) {
					domain.setPro_status(domain.getStatusCurrent().get(s).getStatus());
					domain.setPro_statusKey(domain.getStatusCurrent().get(s).getStatusKey());
				}	
				else if (domain.getStatusCurrent().get(s).getGroupAbbrev().equals(Constants.WG_QTL)) {
					domain.setQtl_status(domain.getStatusCurrent().get(s).getStatus());
					domain.setQtl_statusKey(domain.getStatusCurrent().get(s).getStatusKey());
				}	
				else if (domain.getStatusCurrent().get(s).getGroupAbbrev().equals(Constants.WG_TUMOR)) {
					domain.setTumor_status(domain.getStatusCurrent().get(s).getStatus());
					domain.setTumor_statusKey(domain.getStatusCurrent().get(s).getStatusKey());
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
		List<String> assocDomain = new ArrayList<String>();
		ReferenceAssociatedData flags = entity.getAssociatedData();
		if (flags != null) {
			if (flags.getHas_alleles() != 0) { assocDomain.add("Alleles"); }
			if (flags.getHas_antibodies() != 0) { assocDomain.add("Antibodies"); }
			if (flags.getHas_go() != 0) { assocDomain.add("GO"); }
			if (flags.getHas_gxdindex() != 0) { assocDomain.add("GXD Index"); }
			if (flags.getHas_gxdimages() != 0) { assocDomain.add("GXD/CRE Images"); }
			if (flags.getHas_gxdspecimens() != 0) { assocDomain.add("GXD/CRE Specimens"); }
			if (flags.getHas_gxdresults() != 0) { assocDomain.add("GXD/CRE Assays"); }
			if (flags.getHas_gxdresults() != 0) { assocDomain.add("GXD/CRE Results"); }
			if (flags.getHas_mapping() != 0) { assocDomain.add("Mapping"); }
			if (flags.getHas_markers() != 0) { assocDomain.add("Markers"); }
			if (flags.getHas_probes() != 0) { assocDomain.add("Probes"); }
			if (flags.getHas_strain() != 0) { assocDomain.add("Strain"); }	
			domain.setAssociated_data(assocDomain);
		}
				
		return domain;
	}

}
