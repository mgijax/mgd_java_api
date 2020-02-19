package org.jax.mgi.mgd.api.model.gxd.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.gxd.domain.AntibodyMarkerDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.AntibodyMarker;
import org.jax.mgi.mgd.api.util.Constants;
import org.jboss.logging.Logger;

public class AntibodyMarkerTranslator extends BaseEntityDomainTranslator<AntibodyMarker, AntibodyMarkerDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Override
	protected AntibodyMarkerDomain entityToDomain(AntibodyMarker entity) {

		AntibodyMarkerDomain domain = new AntibodyMarkerDomain();

		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);		
		domain.setAntibodyMarkerKey(String.valueOf(entity.get_antibodymarker_key()));
		domain.setAntibodyKey(String.valueOf(entity.get_antibody_key()));
		domain.setMarkerKey(String.valueOf(entity.getMarker().get_marker_key()));
		domain.setMarkerSymbol(entity.getMarker().getSymbol());
		domain.setChromosome(entity.getMarker().getChromosome());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
		
		return domain;
	}

}
