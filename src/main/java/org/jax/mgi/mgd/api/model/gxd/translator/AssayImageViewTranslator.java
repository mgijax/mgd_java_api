package org.jax.mgi.mgd.api.model.gxd.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.gxd.domain.AssayImageViewDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.AssayImageView;
import org.jax.mgi.mgd.api.util.Constants;
import org.jboss.logging.Logger;

public class AssayImageViewTranslator extends BaseEntityDomainTranslator<AssayImageView, AssayImageViewDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Override
	protected AssayImageViewDomain entityToDomain(AssayImageView entity) {
		AssayImageViewDomain domain = new AssayImageViewDomain();
		
		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
		domain.setAssayKey(String.valueOf(entity.get_assay_key()));
		domain.setImagePaneKey(String.valueOf(entity.get_imagepane_key()));
		domain.setImageKey(String.valueOf(entity.get_image_key()));
		domain.setFigurepaneLabel(entity.getFigurepaneLabel());
		domain.setPaneLabel(entity.getPaneLabel());
		domain.setFigureLabel(entity.getFigureLabel());
		domain.setAccID(entity.getAccID());
		domain.setPixID(entity.getPixID());
		domain.setXDim(entity.getXDim());
		domain.setYDim(entity.getYDim());
		domain.setX(entity.getX());
		domain.setY(entity.getY());
		domain.setWidth(entity.getWidth());
		domain.setHeight(entity.getHeight());
		
		return domain;
	}

}
