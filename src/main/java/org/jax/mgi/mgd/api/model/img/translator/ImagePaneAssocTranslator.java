package org.jax.mgi.mgd.api.model.img.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.img.domain.ImagePaneAssocDomain;
import org.jax.mgi.mgd.api.model.img.entities.ImagePaneAssoc;
import org.jax.mgi.mgd.api.util.Constants;
import org.jboss.logging.Logger;

public class ImagePaneAssocTranslator extends BaseEntityDomainTranslator<ImagePaneAssoc, ImagePaneAssocDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Override
	protected ImagePaneAssocDomain entityToDomain(ImagePaneAssoc entity) {
		ImagePaneAssocDomain domain = new ImagePaneAssocDomain();
		
		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);		
		domain.setAssocKey(String.valueOf(entity.get_assoc_key()));
		domain.setImagePaneKey(String.valueOf(entity.getImagePane().get_imagepane_key()));
		domain.setMgiTypeKey(String.valueOf(entity.getMgiType().get_mgitype_key()));
		domain.setObjectKey(String.valueOf(entity.get_object_key()));
		domain.setIsPrimary(String.valueOf(entity.getIsPrimary()));
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
	
		// alleles or genotypes associated with the image pane must be translated by separate method
		// for example, see AlleleService/getAlleleByImagePane

		return domain;
	}

}
