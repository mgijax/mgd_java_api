package org.jax.mgi.mgd.api.model.gxd.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.gxd.domain.InSituResultImageViewDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.InSituResultImageView;
import org.jax.mgi.mgd.api.util.Constants;
import org.jboss.logging.Logger;

public class InSituResultImageViewTranslator extends BaseEntityDomainTranslator<InSituResultImageView, InSituResultImageViewDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Override
	protected InSituResultImageViewDomain entityToDomain(InSituResultImageView entity) {
		InSituResultImageViewDomain domain = new InSituResultImageViewDomain();
		
		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
		domain.setResultImageKey(String.valueOf(entity.get_resultimage_key()));
		domain.setResultKey(String.valueOf(entity.get_result_key()));
		domain.setImagePaneKey(String.valueOf(entity.get_imagepane_key()));
		domain.setImageKey(String.valueOf(entity.get_image_key()));
		domain.setFigurepaneLabel(entity.getFigurepaneLabel());
		domain.setAccID(entity.getAccID());
		domain.setPixID(entity.getPixID().toString());
		domain.setXDim(entity.getXDim().toString());
		domain.setYDim(entity.getYDim().toString());
		domain.setX(entity.getX().toString());
		domain.setY(entity.getY().toString());
		domain.setWidth(entity.getWidth().toString());
		domain.setHeight(entity.getHeight().toString());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
		
		return domain;
	}

}
