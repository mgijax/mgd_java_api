package org.jax.mgi.mgd.api.model.all.translator;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.all.domain.SlimAlleleDomain;
import org.jax.mgi.mgd.api.model.all.entities.Allele;
import org.jax.mgi.mgd.api.model.mgi.domain.SlimMGIReferenceAssocDomain;
import org.jax.mgi.mgd.api.model.mgi.translator.SlimMGIReferenceAssocTranslator;

public class SlimAlleleTranslator extends BaseEntityDomainTranslator<Allele, SlimAlleleDomain> {

	private SlimMGIReferenceAssocTranslator refAssocTranslator = new SlimMGIReferenceAssocTranslator();
	
	@Override
	protected SlimAlleleDomain entityToDomain(Allele entity) {
		
		SlimAlleleDomain domain = new SlimAlleleDomain();
		
		domain.setAlleleKey(String.valueOf(entity.get_allele_key()));
		domain.setSymbol(entity.getSymbol());
		
		// marker key, symbol, chromosome, strand via mrk_location_cache
		if (entity.getMarker() != null) {
			if (entity.getMarker().getLocationCache() != null) {
				domain.setMarkerKey(String.valueOf(entity.getMarker().get_marker_key()));
				domain.setMarkerSymbol(entity.getMarker().getSymbol());
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
			Iterable<SlimMGIReferenceAssocDomain> i = refAssocTranslator.translateEntities(entity.getRefAssocs());
			if(i.iterator().hasNext() == true) {
				domain.setRefAssocs(IteratorUtils.toList(i.iterator()));
			}
		}
		
		return domain;
	}

}
