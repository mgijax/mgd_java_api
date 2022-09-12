package org.jax.mgi.mgd.api.model.mld.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.mld.domain.SlimExptsDomain;
import org.jax.mgi.mgd.api.model.mld.entities.Expts;
import org.jboss.logging.Logger;

public class SlimExptsTranslator extends BaseEntityDomainTranslator<Expts, SlimExptsDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Override
	protected SlimExptsDomain entityToDomain(Expts entity) {
		
		SlimExptsDomain domain = new SlimExptsDomain();

		domain.setExptKey(String.valueOf(entity.get_expt_key()));
		domain.setExptDisplay(entity.getReference().getJnumid() + ", " + entity.getExptType() + ", Chr " + entity.getChromosome());
		domain.setExptType(entity.getExptType());
		domain.setChromosome(entity.getChromosome());
		domain.setRefsKey(String.valueOf(entity.getReference().get_refs_key()));
		domain.setJnumid(entity.getReference().getJnumid());
		domain.setJnum(entity.getReference().getNumericPart());
		domain.setShort_citation(entity.getReference().getShort_citation());		
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));

		// mgi accession ids only
		if (entity.getMgiAccessionIds() != null && !entity.getMgiAccessionIds().isEmpty()) {
			domain.setAccID(entity.getMgiAccessionIds().get(0).getAccID());
		}
		
		return domain;
	}

}
