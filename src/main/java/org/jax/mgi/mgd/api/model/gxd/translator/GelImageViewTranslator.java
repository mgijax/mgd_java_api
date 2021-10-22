package org.jax.mgi.mgd.api.model.gxd.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.gxd.domain.GelImageViewDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.GelImageView;
import org.jax.mgi.mgd.api.util.Constants;
import org.jboss.logging.Logger;

public class GelImageViewTranslator extends BaseEntityDomainTranslator<GelImageView, GelImageViewDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Override
	protected GelImageViewDomain entityToDomain(GelImageView entity) {
		GelImageViewDomain domain = new GelImageViewDomain();
		
		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
		domain.setImagePaneKey(String.valueOf(entity.get_imagepane_key()));
		domain.setImageKey(String.valueOf(entity.get_image_key()));
		domain.setFigurepaneLabel(entity.getFigurepaneLabel());
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
