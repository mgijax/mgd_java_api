package org.jax.mgi.mgd.api.model.mld.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.mld.domain.ExptMarkerDomain;
import org.jax.mgi.mgd.api.model.mld.entities.ExptMarker;
import org.jax.mgi.mgd.api.util.Constants;
import org.jboss.logging.Logger;

public class ExptMarkerTranslator extends BaseEntityDomainTranslator<ExptMarker, ExptMarkerDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Override
	protected ExptMarkerDomain entityToDomain(ExptMarker entity) {
		
		ExptMarkerDomain domain = new ExptMarkerDomain();

		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);		
		domain.setAssocKey(String.valueOf(entity.get_assoc_key()));
		domain.setExptKey(String.valueOf(entity.get_expt_key()));
		domain.setMarkerKey(String.valueOf(entity.getMarker().get_marker_key()));
		domain.setMarkerSymbol(entity.getMarker().getSymbol());
		domain.setMarkerAccID(entity.getMarker().getMgiAccessionIds().get(0).getAccID());
		domain.setAssayTypeKey(String.valueOf(entity.getAssayType().get_assay_type_key()));
		domain.setAssayType(entity.getAssayType().getDescription());
		domain.setSequenceNum(entity.getSequenceNum());
		domain.setDescription(entity.getDescription());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
		
		if (entity.getAllele() != null) {
			domain.setAlleleKey(String.valueOf(entity.getAllele().get_allele_key()));
			domain.setAlleleSymbol(entity.getAllele().getSymbol());			
		}
		
		return domain;
	}

}
