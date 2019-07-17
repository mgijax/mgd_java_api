package org.jax.mgi.mgd.api.model.img.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.img.domain.ImageSubmissionDomain;
import org.jax.mgi.mgd.api.model.img.entities.Image;
import org.jax.mgi.mgd.api.util.Constants;
import org.jboss.logging.Logger;

public class ImageSubmissionTranslator extends BaseEntityDomainTranslator<Image, ImageSubmissionDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Override
	protected ImageSubmissionDomain entityToDomain(Image entity) {
		
		ImageSubmissionDomain domain = new ImageSubmissionDomain();

		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
		domain.setImageClassKey(String.valueOf(entity.getImageClass().get_term_key()));
		domain.setImageClass(entity.getImageClass().getTerm());
		domain.setImageKey(String.valueOf(entity.get_image_key()));
		domain.setImageTypeKey(String.valueOf(entity.getImageType().get_term_key()));
		domain.setImageType(entity.getImageType().getTerm());		
		domain.setFigureLabel(entity.getFigureLabel());

		// reference
		domain.setRefsKey(String.valueOf(entity.getReference().get_refs_key()));
		domain.setJnumid(entity.getReference().getReferenceCitationCache().getJnumid());
		domain.setShort_citation(entity.getReference().getReferenceCitationCache().getShort_citation());

		if (entity.getXDim() != null) {
			domain.setXDim(String.valueOf(entity.getXDim()));
			domain.setImageStatus("Image file already loaded.");
		}
		else {
			domain.setImageStatus("No file chosen.");	
		}
		
		if (entity.getYDim() != null) {
			domain.setYDim(String.valueOf(entity.getYDim()));
		}

		if (entity.getThumbnailImage() != null) {
			domain.setThumbnailFigureLabel(entity.getThumbnailImage().getFigureLabel());
		}
		
		return domain;
	}

}
