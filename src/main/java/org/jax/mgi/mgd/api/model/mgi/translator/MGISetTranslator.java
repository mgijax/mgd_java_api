package org.jax.mgi.mgd.api.model.mgi.translator;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.mgi.domain.MGISetDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.MGISetMemberDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.MGISet;
import org.jax.mgi.mgd.api.util.Constants;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MGISetTranslator extends BaseEntityDomainTranslator<MGISet, MGISetDomain> {

	private MGISetMemberTranslator setTranslator = new MGISetMemberTranslator();
	
	@Override
	protected MGISetDomain entityToDomain(MGISet entity) {
		MGISetDomain domain = new MGISetDomain();
		
		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
		domain.setSetKey(String.valueOf(entity.get_set_key()));
		domain.setMgiTypeKey(String.valueOf(entity.getMgiType().get_mgitype_key()));
		domain.setSetName(entity.getName());
		domain.setSequenceNum(String.valueOf(entity.getSequenceNum()));
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));

		// one-to-many members of type "term"/_mgitype_key = 13		
		if (entity.getEmapaStageMembers() != null && !entity.getEmapaStageMembers().isEmpty()) {
			Iterable<MGISetMemberDomain> i = setTranslator.translateEntities(entity.getEmapaStageMembers());
			domain.setEmapaStageMembers(IteratorUtils.toList(i.iterator()));
		}

		if (entity.getGenotypeClipboardMembers() != null && !entity.getGenotypeClipboardMembers().isEmpty()) {
			Iterable<MGISetMemberDomain> i = setTranslator.translateEntities(entity.getGenotypeClipboardMembers());
			domain.setGenotypeClipboardMembers(IteratorUtils.toList(i.iterator()));
		}
				
		return domain;
	}

}
