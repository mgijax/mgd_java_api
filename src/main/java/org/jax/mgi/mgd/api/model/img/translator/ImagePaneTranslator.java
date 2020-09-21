package org.jax.mgi.mgd.api.model.img.translator;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
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

		// one-to-many associations
		if (entity.getPaneAssocs() != null && !entity.getPaneAssocs().isEmpty()) {
			ImagePaneAssocTranslator assocTranslator = new ImagePaneAssocTranslator();
			Iterable<ImagePaneAssocDomain> i = assocTranslator.translateEntities(entity.getPaneAssocs());
			domain.setPaneAssocs(IteratorUtils.toList(i.iterator()));
		}
			
		return domain;
	}

}
