package org.jax.mgi.mgd.api.model.mrk.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.mrk.domain.MarkerDomain;
import org.jax.mgi.mgd.api.model.mrk.entities.Marker;

public class MarkerTranslator extends BaseEntityDomainTranslator<Marker, MarkerDomain> {

	@Override
	protected MarkerDomain entityToDomain(Marker entity) {
		MarkerDomain domain = new MarkerDomain();
		domain.set_marker_key(entity.get_marker_key());
		domain.setSymbol(entity.getSymbol());
		domain.setName(entity.getName());
		domain.setChromosome(entity.getChromosome());
		domain.setCytogeneticOffset(entity.getCytogeneticOffset());
		domain.setOrganism(entity.getOrganism().getCommonname());
		domain.setMarkerStatus(entity.getMarkerStatus().getStatus());
		domain.setMarkerType(entity.getMarkerType().getName());
		domain.setCreatedBy(entity.getCreatedBy().getName());
		domain.setModifiedBy(entity.getModifiedBy().getName());
		domain.setMgiAccessionId(entity.getMgiAccessionId().getAccID());
		return domain;
	}

	@Override
	protected Marker domainToEntity(MarkerDomain domain) {
		// TODO Auto-generated method stub
		return null;
	}

}
