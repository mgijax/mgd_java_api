package org.jax.mgi.mgd.api.model.prb.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.prb.domain.ProbeSourceDomain;
import org.jax.mgi.mgd.api.model.prb.domain.ProbeTissueDomain;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeSource;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeTissue;

public class ProbeTissueTranslator extends BaseEntityDomainTranslator<ProbeTissue, ProbeTissueDomain> {

	@Override
	protected ProbeTissueDomain entityToDomain(ProbeTissue entity) {
		
		ProbeTissueDomain domain = new ProbeTissueDomain();

		domain.setTissueKey(String.valueOf(entity.get_tissue_key()));
		domain.setTissue(entity.getTissue());
		domain.setStandard(String.valueOf(entity.getStandard()));
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
				
		return domain;
	}

}
