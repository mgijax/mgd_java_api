package org.jax.mgi.mgd.api.model.bib.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.bib.domain.LTReferenceSummaryDomain;
import org.jax.mgi.mgd.api.model.bib.entities.LTReference;
import org.jax.mgi.mgd.api.util.Constants;

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
		domain.short_citation = entity.getCitationData().get(0).getShort_citation();
		domain.haspdf = String.valueOf(entity.getWorkflowData().get(0).getHaspdf());

		for (int i = 0; i < entity.getWorkflowStatus().size(); i++) {
			if (entity.getWorkflowStatus().get(i).getIsCurrent() == 1) {
				if (entity.getWorkflowStatus().get(i).getGroupTerm().getAbbreviation().equals(Constants.WG_AP)) {
					domain.ap_status = entity.getWorkflowStatus().get(i).getStatusTerm().getAbbreviation();
				}
				else if (entity.getWorkflowStatus().get(i).getGroupTerm().getAbbreviation().equals(Constants.WG_GO)) {
					domain.go_status = entity.getWorkflowStatus().get(i).getStatusTerm().getAbbreviation();
				}
				else if (entity.getWorkflowStatus().get(i).getGroupTerm().getAbbreviation().equals(Constants.WG_GXD)) {
					domain.gxd_status = entity.getWorkflowStatus().get(i).getStatusTerm().getAbbreviation();
				}	
				else if (entity.getWorkflowStatus().get(i).getGroupTerm().getAbbreviation().equals(Constants.WG_PRO)) {
					domain.pro_status = entity.getWorkflowStatus().get(i).getStatusTerm().getAbbreviation();
				}
				else if (entity.getWorkflowStatus().get(i).getGroupTerm().getAbbreviation().equals(Constants.WG_QTL)) {
					domain.qtl_status = entity.getWorkflowStatus().get(i).getStatusTerm().getAbbreviation();
				}
				else if (entity.getWorkflowStatus().get(i).getGroupTerm().getAbbreviation().equals(Constants.WG_TUMOR)) {
					domain.tumor_status = entity.getWorkflowStatus().get(i).getStatusTerm().getAbbreviation();
				}				
			}
		}
		
		return domain;
	}

}
