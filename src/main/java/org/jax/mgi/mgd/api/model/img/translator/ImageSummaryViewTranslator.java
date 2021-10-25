package org.jax.mgi.mgd.api.model.img.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.img.domain.ImageSummaryViewDomain;
import org.jax.mgi.mgd.api.model.img.entities.ImageSummaryView;
import org.jax.mgi.mgd.api.util.Constants;
import org.jboss.logging.Logger;

public class ImageSummaryViewTranslator extends BaseEntityDomainTranslator<ImageSummaryView, ImageSummaryViewDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Override
	protected ImageSummaryViewDomain entityToDomain(ImageSummaryView entity) {
		ImageSummaryViewDomain domain = new ImageSummaryViewDomain();
		
		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
		domain.setImagePaneKey(String.valueOf(entity.get_imagepane_key()));
		domain.setImageKey(String.valueOf(entity.get_image_key()));
		domain.setX(entity.getX());
		domain.setY(entity.getY());
		domain.setWidth(entity.getWidth());
		domain.setHeight(entity.getHeight());
		domain.setAccID(entity.getAccID());
		domain.setPixID(entity.getPixID());
		domain.setXDim(entity.getXDim());
		domain.setYDim(entity.getYDim());
		domain.setFigurepaneLabel(entity.getFigurepaneLabel());

		return domain;
	}

}
