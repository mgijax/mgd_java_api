package org.jax.mgi.mgd.api.model.bib.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.bib.domain.ReferenceSummaryDomain;
import org.jax.mgi.mgd.api.model.bib.entities.Reference;
import org.jax.mgi.mgd.api.model.bib.entities.ReferenceWorkflowData;

public class ReferenceSummaryTranslator extends BaseEntityDomainTranslator<Reference, ReferenceSummaryDomain>{
	@Override
	protected ReferenceSummaryDomain entityToDomain(Reference entity, int translationDepth) {
		ReferenceSummaryDomain domain = new ReferenceSummaryDomain();

		domain._refs_key = entity.get_refs_key();
		domain.title = entity.getTitle();
		domain.jnumid = entity.getCachedID("J:");
		domain.doiid = entity.getCachedID("DOI");
		domain.pubmedid = entity.getCachedID("PubMed");
		domain.mgiid = entity.getCachedID("MGI");
		domain.short_citation = entity.getShort_citation();
		domain.ap_status = entity.getStatusView().getAp_status();
		domain.go_status = entity.getStatusView().getGo_status();
		domain.gxd_status = entity.getStatusView().getGxd_status();
		domain.qtl_status = entity.getStatusView().getQtl_status();
		domain.tumor_status = entity.getStatusView().getTumor_status();

		ReferenceWorkflowData workflowData = entity.getWorkflowData();
		if (workflowData != null) {
			if (workflowData.getHas_pdf() == 0) {
				domain.has_pdf = "No";
			} else {
				domain.has_pdf = "Yes";
			}
		}
		return domain;
	}

	@Override
	protected Reference domainToEntity(ReferenceSummaryDomain domain, int translationDepth) {
		// Cannot do translation here, as it requires lookup of actual entity from database.  Must
		// instead work with ReferenceRepository.

		return null;
	}
}
