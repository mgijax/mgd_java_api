package org.jax.mgi.mgd.api.model.img.translator;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.gxd.domain.GelImageViewDomain;
import org.jax.mgi.mgd.api.model.gxd.translator.GelImageViewTranslator;
import org.jax.mgi.mgd.api.model.img.domain.ImagePaneAssocDomain;
import org.jax.mgi.mgd.api.model.img.domain.ImagePaneDomain;
import org.jax.mgi.mgd.api.model.img.entities.ImagePane;
import org.jax.mgi.mgd.api.util.Constants;
import org.jboss.logging.Logger;

public class ImagePaneTranslator extends BaseEntityDomainTranslator<ImagePane, ImagePaneDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Override
	protected ImagePaneDomain entityToDomain(ImagePane entity) {
		ImagePaneDomain domain = new ImagePaneDomain();
		
		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);		
		domain.setImagePaneKey(String.valueOf(entity.get_imagepane_key()));
		domain.setImageKey(String.valueOf(entity.get_image_key()));
		domain.setPaneLabel(entity.getPaneLabel());
		
		if (entity.getX() != null) {
			domain.setX(String.valueOf(entity.getX()));
		}
				
		if (entity.getY() != null) {
			domain.setY(String.valueOf(entity.getY()));
		}

		if (entity.getWidth() != null) {
			domain.setWidth(String.valueOf(entity.getWidth()));
		}
				
		if (entity.getHeight() != null) {
			domain.setHeight(String.valueOf(entity.getHeight()));
		}
		
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));

		// one-to-many (but really just one) image info
		GelImageViewTranslator imageTranslator = new GelImageViewTranslator();
		Iterable<GelImageViewDomain> i = imageTranslator.translateEntities(entity.getImageSummary());
		domain.setAccID((i.iterator().next().getAccID()));
		domain.setPixID(i.iterator().next().getPixID());
		domain.setFigurepaneLabel(i.iterator().next().getFigurepaneLabel());

		// one-to-many associations
		if (entity.getPaneAssocs() != null && !entity.getPaneAssocs().isEmpty()) {
			ImagePaneAssocTranslator assocTranslator = new ImagePaneAssocTranslator();
			Iterable<ImagePaneAssocDomain> ip = assocTranslator.translateEntities(entity.getPaneAssocs());
			domain.setPaneAssocs(IteratorUtils.toList(ip.iterator()));
		}
		
		return domain;
	}

}
