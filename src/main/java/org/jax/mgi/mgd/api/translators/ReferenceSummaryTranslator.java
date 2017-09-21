package org.jax.mgi.mgd.api.translators;

import org.jax.mgi.mgd.api.domain.ReferenceSummaryDomain;
import org.jax.mgi.mgd.api.entities.Reference;
import org.jax.mgi.mgd.api.entities.ReferenceWorkflowData;
import org.jax.mgi.mgd.api.util.Constants;

public class ReferenceSummaryTranslator extends EntityDomainTranslator<Reference, ReferenceSummaryDomain>{
	@Override
	protected ReferenceSummaryDomain entityToDomain(Reference entity) {
		ReferenceSummaryDomain domain = new ReferenceSummaryDomain();

		domain._refs_key = entity.get_refs_key();
		domain.title = entity.getTitle();
		domain.jnumid = entity.getJnumid();
		domain.doiid = entity.getDoiid();
		domain.pubmedid = entity.getPubmedid();
		domain.mgiid = entity.getMgiid();
		domain.short_citation = entity.getShort_citation();
		domain.ap_status = entity.getStatus(Constants.WG_AP);
		domain.go_status = entity.getStatus(Constants.WG_GO);
		domain.gxd_status = entity.getStatus(Constants.WG_GXD);
		domain.qtl_status = entity.getStatus(Constants.WG_QTL);
		domain.tumor_status = entity.getStatus(Constants.WG_TUMOR);
		
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
	protected Reference domainToEntity(ReferenceSummaryDomain domain) {
		// Cannot do translation here, as it requires lookup of actual entity from database.  Must
		// instead work with ReferenceRepository.
		
		return null;
	}
}
