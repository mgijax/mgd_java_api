package org.jax.mgi.mgd.api.model.img.translator;

import java.util.Comparator;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.all.domain.SlimAlleleImageDomain;
import org.jax.mgi.mgd.api.model.all.translator.SlimAlleleImageTranslator;
import org.jax.mgi.mgd.api.model.gxd.domain.SlimGenotypeImageDomain;
import org.jax.mgi.mgd.api.model.gxd.translator.SlimGenotypeImageTranslator;
import org.jax.mgi.mgd.api.model.img.domain.ImagePaneAssocDomain;
import org.jax.mgi.mgd.api.model.img.entities.ImagePaneAssoc;
import org.jax.mgi.mgd.api.util.Constants;
import org.jboss.logging.Logger;

public class ImagePaneAssocTranslator extends BaseEntityDomainTranslator<ImagePaneAssoc, ImagePaneAssocDomain> {

	protected Logger log = Logger.getLogger(getClass());

	private SlimAlleleImageTranslator alleleTranslator = new SlimAlleleImageTranslator();
	private SlimGenotypeImageTranslator genotypeTranslator = new SlimGenotypeImageTranslator();

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

		// one-to-many allele associations w/ allele info
		//if (entity.getAlleles() != null && !entity.getAlleles().isEmpty()
		//		&& entity.getMgiType().get_mgitype_key() == 11) {
		//	Iterable<SlimAlleleImageDomain> i = alleleTranslator.translateEntities(entity.getAlleles());
		//	domain.setAlleles(IteratorUtils.toList(i.iterator()));
		//	domain.getAlleles().sort(Comparator.comparing(SlimAlleleImageDomain::getSymbol, String.CASE_INSENSITIVE_ORDER));
		//}
		
		if (entity.getAlleles() != null && !entity.getAlleles().isEmpty()) {
			Iterable<SlimAlleleImageDomain> i = alleleTranslator.translateEntities(entity.getAlleles());
			domain.setAlleles(IteratorUtils.toList(i.iterator()));
			domain.getAlleles().sort(Comparator.comparing(SlimAlleleImageDomain::getSymbol, String.CASE_INSENSITIVE_ORDER));			
		}
		
		// one-to-many genotype associations w/ genotype info
		//if (entity.getGenotypes() != null && !entity.getGenotypes().isEmpty()
		//		&& entity.getMgiType().get_mgitype_key() == 12) {
		//	Iterable<SlimGenotypeImageDomain> i = genotypeTranslator.translateEntities(entity.getGenotypes());
		//	domain.setGenotypes(IteratorUtils.toList(i.iterator()));
			//domain.getGenotypes().sort(Comparator.comparing(SlimAlleleImageDomain::getSymbol, String.CASE_INSENSITIVE_ORDER));
		//}

		if (entity.getGenotypes() != null && !entity.getGenotypes().isEmpty()) {
			Iterable<SlimGenotypeImageDomain> i = genotypeTranslator.translateEntities(entity.getGenotypes());
			domain.setGenotypes(IteratorUtils.toList(i.iterator()));
			//domain.getGenotypes().sort(Comparator.comparing(SlimAlleleImageDomain::getSymbol, String.CASE_INSENSITIVE_ORDER));
		}
				
		return domain;
	}

}
