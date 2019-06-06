package org.jax.mgi.mgd.api.model.gxd.translator;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.acc.domain.AccessionDomain;
import org.jax.mgi.mgd.api.model.acc.translator.AccessionTranslator;
import org.jax.mgi.mgd.api.model.gxd.domain.GenotypeDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.Genotype;
import org.jboss.logging.Logger;

public class GenotypeTranslator extends BaseEntityDomainTranslator<Genotype, GenotypeDomain> {

	protected Logger log = Logger.getLogger(getClass());

	private AccessionTranslator accessionTranslator = new AccessionTranslator();
	
	@Override
	protected GenotypeDomain entityToDomain(Genotype entity) {
		
		GenotypeDomain domain = new GenotypeDomain();

		// do not use 'processStatus' because this is a master domain
		// and only 1 master domain record is processed by the create/update endpoint
			
		domain.setGenotypeKey(String.valueOf(entity.get_genotype_key()));
		domain.setStrainKey(String.valueOf(entity.getStrain().get_strain_key()));
		domain.setIsConditional(String.valueOf(entity.getIsConditional()));
		domain.setNote(entity.getNote());
		domain.setExistsAsKey(String.valueOf(entity.getExistsAs().get_term_key()));
		domain.setExistsAs(entity.getExistsAs().getTerm());
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
		
		// mgi accession ids only
		if (entity.getMgiAccessionIds() != null && !entity.getMgiAccessionIds().isEmpty()) {
			Iterable<AccessionDomain> acc = accessionTranslator.translateEntities(entity.getMgiAccessionIds());
			domain.setMgiAccessionIds(IteratorUtils.toList(acc.iterator()));
		}
				
		return domain;
	}

}
