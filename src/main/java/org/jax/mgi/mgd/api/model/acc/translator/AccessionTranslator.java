package org.jax.mgi.mgd.api.model.acc.translator;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.acc.domain.AccessionDomain;
import org.jax.mgi.mgd.api.model.acc.domain.AccessionReferenceDomain;
import org.jax.mgi.mgd.api.model.acc.entities.Accession;
import org.jax.mgi.mgd.api.util.Constants;
import org.jboss.logging.Logger;

public class AccessionTranslator extends BaseEntityDomainTranslator<Accession, AccessionDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	private AccessionReferenceTranslator accessionReferenceTranslator = new AccessionReferenceTranslator();

	@Override
	protected AccessionDomain entityToDomain(Accession entity) {
		AccessionDomain domain = new AccessionDomain();

		//log.info("AccessionTranslator: " + entity.get_accession_key() + "," + entity.get_object_key());
		
		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
		domain.setAccessionKey(String.valueOf(entity.get_accession_key()));
		domain.setLogicaldbKey(String.valueOf(entity.getLogicaldb().get_logicaldb_key()));
		domain.setLogicaldb(entity.getLogicaldb().getName());

		if (entity.getLogicaldb().get_logicaldb_key() == 122) {
			domain.setAkaLogicaldb("(Microarray)");
		}
		else {
			domain.setAkaLogicaldb(null);
		}

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
		if (entity.getReferences() != null && !entity.getReferences().isEmpty()) {
			Iterable<AccessionReferenceDomain> acc = accessionReferenceTranslator.translateEntities(entity.getReferences());
			domain.setReferences(IteratorUtils.toList(acc.iterator()));
		}
		
		return domain;
	}

}
