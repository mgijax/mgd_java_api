package org.jax.mgi.mgd.api.model.dag.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.dag.domain.DagLabelDomain;
import org.jax.mgi.mgd.api.model.dag.entities.DagLabel;

public class DagLabelTranslator extends BaseEntityDomainTranslator<DagLabel, DagLabelDomain> {
	
	@Override
	protected DagLabelDomain entityToDomain(DagLabel entity) {
		DagLabelDomain domain = new DagLabelDomain();
		
		domain.setLabelKey(String.valueOf(entity.get_label_key()));
		domain.setLabel(entity.getLabel());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
		
		return domain;
	}

}
