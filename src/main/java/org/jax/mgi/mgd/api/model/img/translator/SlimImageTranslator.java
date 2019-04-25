package org.jax.mgi.mgd.api.model.img.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.img.domain.SlimImageDomain;
import org.jax.mgi.mgd.api.model.img.entities.Image;
import org.jboss.logging.Logger;

public class SlimImageTranslator extends BaseEntityDomainTranslator<Image, SlimImageDomain> {

	protected Logger log = Logger.getLogger(getClass());
		
	@Override
	protected SlimImageDomain entityToDomain(Image entity, int translationDepth) {
		
		SlimImageDomain domain = new SlimImageDomain();

		domain.setImageKey(String.valueOf(entity.get_image_key()));
		domain.setImageDisplay(
					entity.getReference().getReferenceCitationCache().getJnumid() + ";" +
					entity.getImageType().getTerm() + ";" +
					entity.getFigureLabel());

		return domain;
	}

	@Override
	protected Image domainToEntity(SlimImageDomain domain, int translationDepth) {
		// TODO Auto-generated method stub
		return null;
	}

}
