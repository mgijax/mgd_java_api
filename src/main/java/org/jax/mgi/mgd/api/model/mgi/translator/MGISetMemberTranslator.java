package org.jax.mgi.mgd.api.model.mgi.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.mgi.domain.MGISetMemberDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.MGISetMemberEmapaDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.MGISetMember;
import org.jax.mgi.mgd.api.util.Constants;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MGISetMemberTranslator extends BaseEntityDomainTranslator<MGISetMember, MGISetMemberDomain> {
	
	@Override
	protected MGISetMemberDomain entityToDomain(MGISetMember entity) {
		MGISetMemberDomain domain = new MGISetMemberDomain();
		
		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
		domain.setSetMemberKey(String.valueOf(entity.get_setmember_key()));
		domain.setSetKey(String.valueOf(entity.get_set_key()));		
		domain.setObjectKey(String.valueOf(entity.get_object_key()));		
		domain.setLabel(entity.getLabel());
		domain.setSequenceNum(entity.getSequenceNum());
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));

		// if set = 1055/genotype Clipboard, then translate accid of genotype
		if (entity.getGenotypeAccessionIds() != null && !entity.getGenotypeAccessionIds().isEmpty()) {
			domain.setGenotypeID(entity.getGenotypeAccessionIds().get(0).getAccID());
		}
		
		// setmember/emapa
		if (entity.getEmapas() != null && !entity.getEmapas().isEmpty()) {
			MGISetMemberEmapaTranslator eTranslator = new MGISetMemberEmapaTranslator();
			Iterable<MGISetMemberEmapaDomain> i = eTranslator.translateEntities(entity.getEmapas());
			domain.setEmapa(i.iterator().next());
		}
		
		return domain;
	}

}
