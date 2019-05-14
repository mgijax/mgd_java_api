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
		domain.setImageKey(String.valueOf(entity.get_image_key()));
		domain.setImageClassKey(String.valueOf(entity.getImageClass().get_term_key()));
		domain.setImageClass(entity.getImageClass().getTerm());
		domain.setImageTypeKey(String.valueOf(entity.getImageType().get_term_key()));
		domain.setImageType(entity.getImageType().getTerm());
		domain.setXDim(String.valueOf(entity.getXDim()));
		domain.setYDim(String.valueOf(entity.getYDim()));		
		domain.setFigureLabel(entity.getFigureLabel());

		// reference
		domain.setRefsKey(String.valueOf(entity.getReference().get_refs_key()));
		domain.setJnumid(entity.getReference().getReferenceCitationCache().getJnumid());
		domain.setShort_citation(entity.getReference().getReferenceCitationCache().getShort_citation());

		// may have 1 thumbnail
		if (entity.getThumbnailImage() != null ) {
			ImageSubmissionTranslator submissionTranslator = new ImageSubmissionTranslator();		
			domain.setThumbnailImage(submissionTranslator.translate(entity.getThumbnailImage()));
		}
		
		return domain;
	}

}
