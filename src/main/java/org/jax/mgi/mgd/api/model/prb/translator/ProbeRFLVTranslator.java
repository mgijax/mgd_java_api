package org.jax.mgi.mgd.api.model.prb.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.prb.domain.ProbeRFLVDomain;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeRFLV;
import org.jax.mgi.mgd.api.util.Constants;

public class ProbeRFLVTranslator extends BaseEntityDomainTranslator<ProbeRFLV, ProbeRFLVDomain> {

	@Override
	protected ProbeRFLVDomain entityToDomain(ProbeRFLV entity) {
		
		ProbeRFLVDomain domain = new ProbeRFLVDomain();
		
		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);	
		domain.setRflvKey(String.valueOf(entity.get_rflv_key()));		
		domain.setReferenceKey(String.valueOf(entity.get_reference_key()));
		domain.setMarkerKey(String.valueOf(entity.getMarker().get_marker_key()));
		domain.setSymbol(entity.getMarker().getSymbol());
		domain.setEndonuclease(String.valueOf(entity.getEndonuclease()));
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
		
		return domain;
	}

}
