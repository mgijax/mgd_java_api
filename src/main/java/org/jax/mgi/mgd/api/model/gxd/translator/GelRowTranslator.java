package org.jax.mgi.mgd.api.model.gxd.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.gxd.domain.GelRowDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.GelRow;
import org.jax.mgi.mgd.api.util.Constants;
import org.jboss.logging.Logger;

public class GelRowTranslator extends BaseEntityDomainTranslator<GelRow, GelRowDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Override
	protected GelRowDomain entityToDomain(GelRow entity) {

		GelRowDomain domain = new GelRowDomain();

		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
		domain.setGelRowKey(String.valueOf(entity.get_gelrow_key()));
		domain.setAssayKey(String.valueOf(entity.get_assay_key()));
		domain.setGelUnitsKey(String.valueOf(entity.getGelUnits().get_term_key()));
		domain.setGelUnits(entity.getGelUnits().getTerm());
		domain.setSequenceNum(entity.getSequenceNum());
		domain.setSize(entity.getSize());
		domain.setRowNote(entity.getRowNote());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
		
		return domain;
	}

}
