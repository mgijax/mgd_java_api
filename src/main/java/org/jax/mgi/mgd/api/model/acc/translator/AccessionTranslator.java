package org.jax.mgi.mgd.api.model.acc.translator;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.acc.domain.AccessionDomain;
import org.jax.mgi.mgd.api.model.acc.domain.AccessionReferenceDomain;
import org.jax.mgi.mgd.api.model.acc.entities.Accession;
import org.jax.mgi.mgd.api.model.acc.service.AccessionService;
import org.jax.mgi.mgd.api.util.Constants;
import org.jboss.logging.Logger;

public class AccessionTranslator extends BaseEntityDomainTranslator<Accession, AccessionDomain> {

	protected Logger log = Logger.getLogger(AccessionService.class);
	
	private AccessionReferenceTranslator accessionReferenceTranslator = new AccessionReferenceTranslator();

	@Override
	protected AccessionDomain entityToDomain(Accession entity, int translationDepth) {
		AccessionDomain domain = new AccessionDomain();

		//log.info("AccessionTranslator: " + entity.get_accession_key() + "," + entity.get_object_key());
		
		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
		domain.setAccessionKey(String.valueOf(entity.get_accession_key()));
		domain.setLogicaldbKey(String.valueOf(entity.getLogicaldb().get_logicaldb_key()));
		domain.setLogicaldb(entity.getLogicaldb().getName());
		domain.setObjectKey(String.valueOf(entity.get_object_key()));
		domain.setMgiTypeKey(String.valueOf(entity.getMgiType().get_mgitype_key()));
		
		domain.setAccID(entity.getAccID());
		domain.setPrefixPart(entity.getPrefixPart());
		domain.setNumericPart(String.valueOf(entity.getNumericPart()));	
		domain.setIsPrivate(String.valueOf(entity.getIsPrivate()));
		domain.setPreferred(String.valueOf(entity.getPreferred()));
		
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));

		// list of references
		if (entity.getReferences() != null) {
			Iterable<AccessionReferenceDomain> acc = accessionReferenceTranslator.translateEntities(entity.getReferences());
			if(acc.iterator().hasNext() == true) {
				domain.setReferences(IteratorUtils.toList(acc.iterator()));
			}
		}
		
		return domain;
	}

	@Override
	protected Accession domainToEntity(AccessionDomain domain, int translationDepth) {
		// Needs to be implemented once we choose to save terms
		return null;
	}

}
