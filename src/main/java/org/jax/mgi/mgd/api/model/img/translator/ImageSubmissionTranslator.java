package org.jax.mgi.mgd.api.model.img.translator;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.img.domain.ImagePaneDomain;
import org.jax.mgi.mgd.api.model.img.domain.ImageSubmissionDomain;
import org.jax.mgi.mgd.api.model.img.entities.Image;
import org.jax.mgi.mgd.api.util.Constants;
import org.jboss.logging.Logger;

public class ImageSubmissionTranslator extends BaseEntityDomainTranslator<Image, ImageSubmissionDomain> {

	protected Logger log = Logger.getLogger(getClass());

	private ImagePaneTranslator imagePaneTranslator = new ImagePaneTranslator();

	@Override
	protected ImageSubmissionDomain entityToDomain(Image entity) {
		
		ImageSubmissionDomain domain = new ImageSubmissionDomain();

		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);				
		domain.setImageKey(String.valueOf(entity.get_image_key()));
		domain.setImageClassKey(String.valueOf(entity.getImageClass().get_term_key()));
		domain.setImageClass(entity.getImageClass().getTerm());
		domain.setImageTypeKey(String.valueOf(entity.getImageType().get_term_key()));
		domain.setImageType(entity.getImageType().getTerm());
		domain.setFigureLabel(entity.getFigureLabel());

		// reference
		domain.setRefsKey(String.valueOf(entity.getReference().get_refs_key()));
		domain.setJnumid(entity.getReference().getReferenceCitationCache().getJnumid());
		domain.setShort_citation(entity.getReference().getReferenceCitationCache().getShort_citation());

		// image panes 
		if (entity.getImagePanes() != null && !entity.getImagePanes().isEmpty()) {
			Iterable<ImagePaneDomain> imagePane = imagePaneTranslator.translateEntities(entity.getImagePanes());
			domain.setImagePanes(IteratorUtils.toList(imagePane.iterator()));
		}
			
		return domain;
	}

}
