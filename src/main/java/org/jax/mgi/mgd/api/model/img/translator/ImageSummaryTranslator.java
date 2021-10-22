package org.jax.mgi.mgd.api.model.img.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.img.domain.ImageSummaryDomain;
import org.jax.mgi.mgd.api.model.img.entities.ImageSummary;
import org.jboss.logging.Logger;

public class ImageSummaryTranslator extends BaseEntityDomainTranslator<ImageSummary, ImageSummaryDomain> {

	protected Logger log = Logger.getLogger(getClass());
		
	@Override
	protected ImageSummaryDomain entityToDomain(ImageSummary entity) {
		
		ImageSummaryDomain domain = new ImageSummaryDomain();

		domain.setImageKey(String.valueOf(entity.get_image_key()));
		domain.setAccID(entity.getAccID());
		domain.setFigurePaneLabel(entity.getShort_description());
		
		return domain;
	}

}
