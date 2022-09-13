package org.jax.mgi.mgd.api.model.prb.translator;

import java.util.Comparator;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.prb.domain.ProbeAlleleDomain;
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
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
		
		// rflv/alleles
		if (entity.getRflvAlleles() != null && !entity.getRflvAlleles().isEmpty()) {
			ProbeAlleleTranslator rflvTranslator = new ProbeAlleleTranslator();
			Iterable<ProbeAlleleDomain> rflv = rflvTranslator.translateEntities(entity.getRflvAlleles());
			domain.setRflvAlleles(IteratorUtils.toList(rflv.iterator()));
			domain.getRflvAlleles().sort(Comparator.comparing(ProbeAlleleDomain::getAllele));						
		}
		
		return domain;
	}

}
