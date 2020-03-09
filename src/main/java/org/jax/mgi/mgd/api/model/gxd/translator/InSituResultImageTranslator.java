package org.jax.mgi.mgd.api.model.gxd.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.gxd.domain.InSituResultImageDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.InSituResultImage;
import org.jax.mgi.mgd.api.util.Constants;
import org.jboss.logging.Logger;

public class InSituResultImageTranslator extends BaseEntityDomainTranslator<InSituResultImage, InSituResultImageDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Override
	protected InSituResultImageDomain entityToDomain(InSituResultImage entity) {

		InSituResultImageDomain domain = new InSituResultImageDomain();

		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
		domain.setResultImageKey(String.valueOf(entity.get_resultimage_key()));				
		domain.setResultKey(String.valueOf(entity.get_result_key()));
		domain.setImagePaneKey(String.valueOf(entity.getImagePane().get_imagepane_key()));
		domain.setImagePane(entity.getImagePane().getPaneLabel());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
		
		return domain;
	}

}
