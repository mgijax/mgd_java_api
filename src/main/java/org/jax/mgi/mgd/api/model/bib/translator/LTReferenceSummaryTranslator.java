package org.jax.mgi.mgd.api.model.bib.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.bib.domain.LTReferenceSummaryDomain;
import org.jax.mgi.mgd.api.model.bib.entities.LTReference;
import org.jax.mgi.mgd.api.model.bib.entities.LTReferenceWorkflowData;

public class LTReferenceSummaryTranslator extends BaseEntityDomainTranslator<LTReference, LTReferenceSummaryDomain>{
	@Override
	protected LTReferenceSummaryDomain entityToDomain(LTReference entity) {
		if (entity == null) { return null; }
		LTReferenceSummaryDomain domain = new LTReferenceSummaryDomain();

		domain.refsKey = String.valueOf(entity.get_refs_key());
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

		LTReferenceWorkflowData workflowData = entity.getWorkflowData();
		if (workflowData != null) {
			if (workflowData.getHas_pdf() == 0) {
				domain.has_pdf = "No";
			} else {
				domain.has_pdf = "Yes";
			}
		}
		return domain;
	}

}
