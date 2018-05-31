package org.jax.mgi.mgd.api.model.mrk.translator;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.all.domain.AlleleDomain;
import org.jax.mgi.mgd.api.model.all.translator.AlleleTranslator;
import org.jax.mgi.mgd.api.model.mrk.domain.MarkerDomain;
import org.jax.mgi.mgd.api.model.mrk.entities.Marker;

public class MarkerTranslator extends BaseEntityDomainTranslator<Marker, MarkerDomain> {

	private AlleleTranslator alleleTranslator = new AlleleTranslator();
	
	@Override
	protected MarkerDomain entityToDomain(Marker entity) {
		MarkerDomain domain = new MarkerDomain();
		domain.setMarkerKey(entity.get_marker_key());
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

		Iterable<AlleleDomain> alleles = alleleTranslator.translateEntities(entity.getAlleles());
		domain.setAlleles(IteratorUtils.toList(alleles.iterator()));
		
		return domain;
	}

	@Override
	protected Marker domainToEntity(MarkerDomain domain) {
		// TODO Auto-generated method stub
		return null;
	}

}
