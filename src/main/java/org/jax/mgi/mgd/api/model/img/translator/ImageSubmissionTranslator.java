package org.jax.mgi.mgd.api.model.img.translator;

import java.util.Comparator;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.acc.domain.AccessionDomain;
import org.jax.mgi.mgd.api.model.acc.translator.AccessionTranslator;
import org.jax.mgi.mgd.api.model.img.domain.ImageSubmissionDomain;
import org.jax.mgi.mgd.api.model.img.entities.Image;
import org.jax.mgi.mgd.api.util.Constants;
import org.jboss.logging.Logger;

public class ImageSubmissionTranslator extends BaseEntityDomainTranslator<Image, ImageSubmissionDomain> {

	protected Logger log = Logger.getLogger(getClass());

	private AccessionTranslator accessionTranslator = new AccessionTranslator();
	
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
			domain.setHasPixId(true);
			domain.setPixStatus("Image file already loaded.");
		}
		else {
			domain.setHasPixId(false);
			domain.setPixStatus("No file chosen.");			
		}
		
		if (entity.getYDim() != null) {
			domain.setYDim(String.valueOf(entity.getYDim()));
		}

		if (entity.getThumbnailImage() != null) {
			domain.setThumbnailFigureLabel(entity.getThumbnailImage().getFigureLabel());
		}
		
		// accession ids editable
		if (entity.getEditAccessionIds() != null && !entity.getEditAccessionIds().isEmpty()) {
			Iterable<AccessionDomain> acc = accessionTranslator.translateEntities(entity.getEditAccessionIds());
			domain.setPixIds(IteratorUtils.toList(acc.iterator()));
			domain.getPixIds().sort(Comparator.comparing(AccessionDomain::getLogicaldb).thenComparing(AccessionDomain::getAccID));
		}
		
		return domain;
	}

}
