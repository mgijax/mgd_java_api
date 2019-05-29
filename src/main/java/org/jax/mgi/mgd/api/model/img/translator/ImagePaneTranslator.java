package org.jax.mgi.mgd.api.model.img.translator;

import java.util.Comparator;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.all.domain.SlimAlleleImageDomain;
import org.jax.mgi.mgd.api.model.img.domain.ImagePaneAssocDomain;
import org.jax.mgi.mgd.api.model.img.domain.ImagePaneDomain;
import org.jax.mgi.mgd.api.model.img.entities.ImagePane;
import org.jax.mgi.mgd.api.util.Constants;
import org.jboss.logging.Logger;

public class ImagePaneTranslator extends BaseEntityDomainTranslator<ImagePane, ImagePaneDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	private ImagePaneAssocTranslator assocTranslator = new ImagePaneAssocTranslator();

	@Override
	protected ImagePaneDomain entityToDomain(ImagePane entity) {
		ImagePaneDomain domain = new ImagePaneDomain();
		
		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);		
		domain.setImagePaneKey(String.valueOf(entity.get_imagepane_key()));
		domain.setImageKey(String.valueOf(entity.get_image_key()));
		domain.setPaneLabel(entity.getPaneLabel());
		domain.setX(String.valueOf(entity.getX()));
		domain.setY(String.valueOf(entity.getY()));
		domain.setWidth(String.valueOf(entity.getWidth()));
		domain.setHeight(String.valueOf(entity.getHeight()));
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));

		// one-to-many allele associations
		if (entity.getAlleleAssocs() != null && !entity.getAlleleAssocs().isEmpty()) {
			Iterable<ImagePaneAssocDomain> i = assocTranslator.translateEntities(entity.getAlleleAssocs());
			domain.setAlleleAssocs(IteratorUtils.toList(i.iterator()));
			domain.getAlleleAssocs().get(0).getAlleles().sort(Comparator.comparing(SlimAlleleImageDomain::getSymbol, String.CASE_INSENSITIVE_ORDER));
			//domain.getAlleleAssocs().forEach(action);
		}
			
		return domain;
	}

}
