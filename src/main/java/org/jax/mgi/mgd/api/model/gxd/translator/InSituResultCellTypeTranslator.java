package org.jax.mgi.mgd.api.model.gxd.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.gxd.domain.InSituResultCellTypeDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.InSituResultCellType;
import org.jax.mgi.mgd.api.util.Constants;
import org.jboss.logging.Logger;

public class InSituResultCellTypeTranslator extends BaseEntityDomainTranslator<InSituResultCellType, InSituResultCellTypeDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Override
	protected InSituResultCellTypeDomain entityToDomain(InSituResultCellType entity) {

		InSituResultCellTypeDomain domain = new InSituResultCellTypeDomain();

		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
		domain.setResultCelltypeKey(String.valueOf(entity.get_resultcelltype_key()));				
		domain.setResultKey(String.valueOf(entity.get_result_key()));				
		domain.setCelltypeTermKey(String.valueOf(entity.getCelltypeTerm().get_term_key()));
		domain.setCelltypeTerm(entity.getCelltypeTerm().getTerm());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
		
		return domain;
	}

}
