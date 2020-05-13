package org.jax.mgi.mgd.api.model.mgi.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.mgi.domain.RelationshipAlleleDriverGeneDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.RelationshipAlleleDriverGene;
import org.jax.mgi.mgd.api.util.Constants;

public class RelationshipAlleleDriverGeneTranslator extends BaseEntityDomainTranslator<RelationshipAlleleDriverGene, RelationshipAlleleDriverGeneDomain> {
		
	@Override
	protected RelationshipAlleleDriverGeneDomain entityToDomain(RelationshipAlleleDriverGene entity) {	
		RelationshipAlleleDriverGeneDomain domain = new RelationshipAlleleDriverGeneDomain();

		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);		
		domain.setRelationshipKey(String.valueOf(entity.get_relationship_key()));
		domain.setAlleleKey(String.valueOf(entity.get_allele_key()));
		domain.setMarkerKey(String.valueOf(entity.get_marker_key()));
		domain.setOrganismKey(String.valueOf(entity.get_organism_key()));
		domain.setMarkerSymbol(entity.getSymbol());	
		domain.setCommonName(entity.getCommonName());
		
		return domain;
	}

}
