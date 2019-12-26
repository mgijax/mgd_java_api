package org.jax.mgi.mgd.api.model.all.translator;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.acc.domain.SlimAccessionDomain;
import org.jax.mgi.mgd.api.model.acc.translator.SlimAccessionTranslator;
import org.jax.mgi.mgd.api.model.all.domain.SlimAlleleRefAssocDomain;
import org.jax.mgi.mgd.api.model.all.entities.Allele;
import org.jax.mgi.mgd.api.model.mgi.domain.SlimMGIReferenceAssocDomain;
import org.jax.mgi.mgd.api.model.mgi.translator.SlimMGIReferenceAssocTranslator;

public class SlimAlleleRefAssocTranslator extends BaseEntityDomainTranslator<Allele, SlimAlleleRefAssocDomain> {
	
	@Override
	protected SlimAlleleRefAssocDomain entityToDomain(Allele entity) {
		
		SlimAlleleRefAssocDomain domain = new SlimAlleleRefAssocDomain();
		
		domain.setAlleleKey(String.valueOf(entity.get_allele_key()));
		domain.setSymbol(entity.getSymbol());

		// marker key, symbol, chromosome, strand via mrk_location_cache
		if (entity.getMarker() != null) {
			if (entity.getMarker().getLocationCache() != null) {
				domain.setMarkerKey(String.valueOf(entity.getMarker().get_marker_key()));
				domain.setMarkerSymbol(entity.getMarker().getSymbol());
				domain.setAlleleStatus(entity.getAlleleStatus().getTerm());
				domain.setChromosome(entity.getMarker().getChromosome());
				domain.setStrand(entity.getMarker().getLocationCache().getStrand());				
			}
		}
		
		// allele mgi id
		if (entity.getMgiAccessionIds() != null) {
			domain.setAccID(entity.getMgiAccessionIds().get(0).getAccID());
		}
		
		// reference associations
		if (!entity.getRefAssocs().isEmpty()) {
			SlimMGIReferenceAssocTranslator refAssocTranslator = new SlimMGIReferenceAssocTranslator();
			Iterable<SlimMGIReferenceAssocDomain> i = refAssocTranslator.translateEntities(entity.getRefAssocs());
			if(i.iterator().hasNext() == true) {
				domain.setRefAssocs(IteratorUtils.toList(i.iterator()));
			}
		}

		return domain;
	}

}
