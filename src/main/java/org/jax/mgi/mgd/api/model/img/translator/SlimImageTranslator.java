package org.jax.mgi.mgd.api.model.img.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.img.domain.SlimImageDomain;
import org.jax.mgi.mgd.api.model.img.entities.Image;
import org.jboss.logging.Logger;

public class SlimImageTranslator extends BaseEntityDomainTranslator<Image, SlimImageDomain> {

	protected Logger log = Logger.getLogger(getClass());
		
	@Override
	protected SlimImageDomain entityToDomain(Image entity) {
		
		SlimImageDomain domain = new SlimImageDomain();

		domain.setImageKey(String.valueOf(entity.get_image_key()));
		domain.setImageDisplay(
					entity.getReference().getJnumid() + ";" +
					entity.getImageType().getTerm() + ";" +
					entity.getFigureLabel());
		
                domain.setFigureLabel(entity.getFigureLabel());
                domain.setImageType(entity.getImageType().getTerm());
                //domain.setNumPanes(entity.getImagePanes().size());
		return domain;
	}

}
