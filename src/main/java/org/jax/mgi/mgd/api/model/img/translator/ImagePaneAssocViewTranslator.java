package org.jax.mgi.mgd.api.model.img.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.img.domain.ImagePaneAssocViewDomain;
import org.jax.mgi.mgd.api.model.img.entities.ImagePaneAssocView;
import org.jax.mgi.mgd.api.util.Constants;
import org.jboss.logging.Logger;

public class ImagePaneAssocViewTranslator extends BaseEntityDomainTranslator<ImagePaneAssocView, ImagePaneAssocViewDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Override
	protected ImagePaneAssocViewDomain entityToDomain(ImagePaneAssocView entity) {
		ImagePaneAssocViewDomain domain = new ImagePaneAssocViewDomain();
		
		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
		domain.setAssocKey(String.valueOf(entity.get_assoc_key()));
		domain.setImagePaneKey(String.valueOf(entity.getImagePane().get_imagepane_key()));
		domain.setMgiTypeKey(String.valueOf(entity.getMgiType().get_mgitype_key()));
		domain.setObjectKey(String.valueOf(entity.get_object_key()));
		domain.setIsPrimary(String.valueOf(entity.getIsPrimary()));
		domain.setFigureLabel(entity.getFigureLabel());
		domain.setImageClass(entity.getTerm());
		domain.setMgiID(entity.getMgiID());
		domain.setPixID(entity.getPixID());
		
		return domain;
	}

}
