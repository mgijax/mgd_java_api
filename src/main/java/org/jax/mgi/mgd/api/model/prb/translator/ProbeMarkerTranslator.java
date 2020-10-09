package org.jax.mgi.mgd.api.model.prb.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.prb.domain.ProbeMarkerDomain;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeMarker;
import org.jax.mgi.mgd.api.util.Constants;

public class ProbeMarkerTranslator extends BaseEntityDomainTranslator<ProbeMarker, ProbeMarkerDomain> {

	@Override
	protected ProbeMarkerDomain entityToDomain(ProbeMarker entity) {
		
		ProbeMarkerDomain domain = new ProbeMarkerDomain();
		
		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
		domain.setAssocKey(String.valueOf(entity.get_assoc_key()));
		domain.setProbeKey(String.valueOf(entity.get_probe_key()));
		domain.setMarkerKey(String.valueOf(entity.getMarker().get_marker_key()));
		domain.setMarkerSymbol(entity.getMarker().getSymbol());
		domain.setMarkerChromosome(entity.getMarker().getChromosome());
		domain.setRefsKey(String.valueOf(entity.getReference().get_refs_key()));
		domain.setRefsKey(String.valueOf(entity.getReference().get_refs_key()));
		domain.setJnumid(entity.getReference().getReferenceCitationCache().getJnumid());
		domain.setJnum(entity.getReference().getReferenceCitationCache().getNumericPart());
		domain.setShort_citation(entity.getReference().getReferenceCitationCache().getShort_citation());
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
		
		if (entity.getRelationship().isEmpty()) {
			domain.setRelationship("(none)");			
		}
		else {
			domain.setRelationship(entity.getRelationship());			
		}
		
		return domain;
	}

}
