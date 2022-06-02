package org.jax.mgi.mgd.api.model.gxd.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.gxd.domain.GXDIndexStageDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.GXDIndexStage;
import org.jax.mgi.mgd.api.util.Constants;
import org.jboss.logging.Logger;

public class GXDIndexStageTranslator extends BaseEntityDomainTranslator<GXDIndexStage, GXDIndexStageDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Override
	protected GXDIndexStageDomain entityToDomain(GXDIndexStage entity) {

		GXDIndexStageDomain domain = new GXDIndexStageDomain();

		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
		domain.setIndexStageKey(String.valueOf(entity.get_indexstage_key()));
		domain.setIndexKey(String.valueOf(entity.get_index_key()));
		domain.setIndexAssayKey(String.valueOf(entity.get_indexassay_key()));
		domain.setStageidKey(String.valueOf(entity.getStageid().get_term_key()));
		domain.setStageid(entity.getStageid().getTerm());
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));

		return domain;
	}

}
