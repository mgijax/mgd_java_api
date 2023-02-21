package org.jax.mgi.mgd.api.model.mgi.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.mgi.domain.MGISetDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.MGISet;
import org.jboss.logging.Logger;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MGISetTranslator extends BaseEntityDomainTranslator<MGISet, MGISetDomain> {

	private MGISetMemberTranslator setTranslator = new MGISetMemberTranslator();
	protected Logger log = Logger.getLogger(getClass());

	@Override
	protected MGISetDomain entityToDomain(MGISet entity) {
		MGISetDomain domain = new MGISetDomain();
		
		log.info("entity.get_set_key:" + entity.get_set_key());
		domain.setSetKey(String.valueOf(entity.get_set_key()));
		//domain.setMgiTypeKey(String.valueOf(entity.getMgiType().get_mgitype_key()));
		log.info("entity.getName:" + entity.getName());
		domain.setSetName(entity.getName());
		//domain.setSequenceNum(String.valueOf(entity.getSequenceNum()));
		//domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		log.info("entity.getCreatedBy:" + entity.getCreatedBy().getLogin());
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		//domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		//domain.setModifiedBy(entity.getModifiedBy().getLogin());
		//domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		//domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
		
		// emapaStageMembers, genotypeClipboardMembers : loaded by MGISetService
		
		return domain;
	}

}
