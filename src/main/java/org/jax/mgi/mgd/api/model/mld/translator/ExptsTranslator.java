package org.jax.mgi.mgd.api.model.mld.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.mld.domain.ExptsDomain;
import org.jax.mgi.mgd.api.model.mld.entities.Expts;
import org.jboss.logging.Logger;

public class ExptsTranslator extends BaseEntityDomainTranslator<Expts, ExptsDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Override
	protected ExptsDomain entityToDomain(Expts entity) {
		
		ExptsDomain domain = new ExptsDomain();

		domain.setExptKey(String.valueOf(entity.get_expt_key()));
		domain.setExptType(entity.getExptType());
		domain.setTag(entity.getTag());		
		domain.setRefsKey(String.valueOf(entity.getReference().get_refs_key()));
		domain.setRefsKey(String.valueOf(entity.getReference().get_refs_key()));
		domain.setJnumid(entity.getReference().getReferenceCitationCache().getJnumid());
		domain.setJnum(entity.getReference().getReferenceCitationCache().getNumericPart());
		domain.setShort_citation(entity.getReference().getReferenceCitationCache().getShort_citation());		
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));

		// mgi accession ids only
//		if (entity.getMgiAccessionIds() != null && !entity.getMgiAccessionIds().isEmpty()) {
//			domain.setAccID(entity.getMgiAccessionIds().get(0).getAccID());
//			SlimAccessionTranslator accessionTranslator = new SlimAccessionTranslator();			
//			Iterable<SlimAccessionDomain> acc = accessionTranslator.translateEntities(entity.getMgiAccessionIds());
//			domain.setMgiAccessionIds(IteratorUtils.toList(acc.iterator()));
//		}
		
		return domain;
	}

}
