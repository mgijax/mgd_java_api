package org.jax.mgi.mgd.api.model.gxd.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.gxd.domain.GelBandDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.GelBand;
import org.jax.mgi.mgd.api.util.Constants;
import org.jboss.logging.Logger;

public class GelBandTranslator extends BaseEntityDomainTranslator<GelBand, GelBandDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Override
	protected GelBandDomain entityToDomain(GelBand entity) {

		GelBandDomain domain = new GelBandDomain();

		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
		domain.setGelBandKey(String.valueOf(entity.get_gelband_key()));
		domain.setGelLaneKey(String.valueOf(entity.get_gellane_key()));	
		domain.setStrengthKey(String.valueOf(entity.getStrength().get_strength_key()));
		domain.setStrength(entity.getStrength().getStrength());
		domain.setGelRowKey(String.valueOf(entity.getGelRow().get_gelrow_key()));
		domain.setAssayKey(String.valueOf(entity.getGelRow().get_assay_key()));
		domain.setGelUnitsKey(String.valueOf(entity.getGelRow().getGelUnits().get_gelunits_key()));
		domain.setGelUnits(entity.getGelRow().getGelUnits().getUnits());
		domain.setSequenceNum(String.valueOf(entity.getGelRow().getSequenceNum()));
		domain.setSize(String.valueOf(entity.getGelRow().getSize()));
		domain.setRowNote(entity.getGelRow().getRowNote());
		domain.setBandNote(entity.getBandNote());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
		
		return domain;
	}

}
