package org.jax.mgi.mgd.api.model.mrk.translator;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.all.domain.AlleleDomain;
import org.jax.mgi.mgd.api.model.all.translator.AlleleTranslator;
import org.jax.mgi.mgd.api.model.gxd.domain.AntibodyDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.AssayDomain;
import org.jax.mgi.mgd.api.model.gxd.translator.AntibodyTranslator;
import org.jax.mgi.mgd.api.model.gxd.translator.AssayTranslator;
import org.jax.mgi.mgd.api.model.mrk.domain.MarkerDomain;
import org.jax.mgi.mgd.api.model.mrk.entities.Marker;
import org.jax.mgi.mgd.api.model.prb.domain.ProbeDomain;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeMarker;
import org.jax.mgi.mgd.api.model.prb.translator.ProbeTranslator;

public class MarkerTranslator extends BaseEntityDomainTranslator<Marker, MarkerDomain> {

	private AlleleTranslator alleleTranslator = new AlleleTranslator();
	private AssayTranslator assayTranslator = new AssayTranslator();
	private ProbeTranslator probeTranslator = new ProbeTranslator();
	private AntibodyTranslator antibodyTranslator = new AntibodyTranslator();

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
		//domain.setMgiAccessionId(entity.getMgiAccessionId().getAccID());

		if(translationDepth > 0) {
			Iterable<AlleleDomain> alleles = alleleTranslator.translateEntities(entity.getAlleles(), translationDepth - 1);
			domain.setAlleles(IteratorUtils.toList(alleles.iterator()));
			
			Iterable<AssayDomain> assays = assayTranslator.translateEntities(entity.getAssays(), translationDepth - 1);
			domain.setAssays(IteratorUtils.toList(assays.iterator()));
			
			List<ProbeDomain> probes = new ArrayList<ProbeDomain>();
			for (ProbeMarker pm : entity.getProbeMarkers()) {
				probes.add(probeTranslator.translate(pm.getProbe()));
			}
			domain.setProbes(probes);
			
			Iterable<AntibodyDomain> antibodies = antibodyTranslator.translateEntities(entity.getAntibodies(), translationDepth - 1);
			domain.setAntibodies(IteratorUtils.toList(antibodies.iterator()));
		}
		
		return domain;
	}

	@Override
	protected Marker domainToEntity(MarkerDomain domain, int translationDepth) {
		// TODO Auto-generated method stub
		return null;
	}

}
