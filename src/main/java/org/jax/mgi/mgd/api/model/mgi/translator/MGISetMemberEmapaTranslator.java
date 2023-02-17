package org.jax.mgi.mgd.api.model.mgi.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.mgi.domain.MGISetMemberEmapaDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.MGISetMemberEmapa;
import org.jax.mgi.mgd.api.util.Constants;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MGISetMemberEmapaTranslator extends BaseEntityDomainTranslator<MGISetMemberEmapa, MGISetMemberEmapaDomain> {
	
	@Override
	protected MGISetMemberEmapaDomain entityToDomain(MGISetMemberEmapa entity) {
		MGISetMemberEmapaDomain domain = new MGISetMemberEmapaDomain();
		
		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
		domain.setSetMemberEmapaKey(String.valueOf(entity.get_setmember_emapa_key()));
		domain.setSetMemberKey(String.valueOf(entity.get_setmember_key()));
		domain.setStage(String.valueOf(entity.getTheilerStage().getStage()));	
		
		// these fields are in the domain, but not translated 
		// because they are not in the entity.
		// therefore, they will remain null if the translator is called
//		domain.setSetKey(String.valueOf(entity.get_set_key()));		
//		domain.setObjectKey(String.valueOf(entity.get_object_key()));	
//		domain.setDisplayIt;
//		domain.setTerm;
		
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
		
		return domain;
	}

}
