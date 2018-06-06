package org.jax.mgi.mgd.api.model.mrk.translator;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.all.domain.AlleleDomain;
import org.jax.mgi.mgd.api.model.all.translator.AlleleTranslator;
import org.jax.mgi.mgd.api.model.mrk.domain.MarkerDomain;
import org.jax.mgi.mgd.api.model.mrk.entities.Marker;
import org.jax.mgi.mgd.api.model.prb.domain.ProbeDomain;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeMarker;
import org.jax.mgi.mgd.api.model.prb.translator.ProbeTranslator;

public class MarkerTranslator extends BaseEntityDomainTranslator<Marker, MarkerDomain> {

	private AlleleTranslator alleleTranslator = new AlleleTranslator();
	private ProbeTranslator probeTranslator = new ProbeTranslator();

	@Override
	protected MarkerDomain entityToDomain(Marker entity, int translationDepth) {
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

		if(translationDepth > 0) {
			Iterable<AlleleDomain> alleles = alleleTranslator.translateEntities(entity.getAlleles(), translationDepth - 1);
			domain.setAlleles(IteratorUtils.toList(alleles.iterator()));
			
			List<ProbeDomain> probes = new ArrayList<ProbeDomain>();
			for (ProbeMarker pm : entity.getProbeMarkers()) {
				probes.add(probeTranslator.translate(pm.getProbe()));
			}
			domain.setProbes(probes);
		}
		
		return domain;
	}

	@Override
	protected Marker domainToEntity(MarkerDomain domain, int translationDepth) {
		// TODO Auto-generated method stub
		return null;
	}

}
