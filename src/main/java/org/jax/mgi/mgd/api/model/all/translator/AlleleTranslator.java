package org.jax.mgi.mgd.api.model.all.translator;

import java.util.Comparator;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.all.domain.AlleleDomain;
import org.jax.mgi.mgd.api.model.all.domain.AlleleMutationDomain;
import org.jax.mgi.mgd.api.model.all.entities.Allele;
import org.jax.mgi.mgd.api.model.mgi.domain.MGIReferenceAssocDomain;
import org.jax.mgi.mgd.api.model.mgi.translator.MGIReferenceAssocTranslator;
import org.jax.mgi.mgd.api.model.voc.domain.AnnotationDomain;
import org.jax.mgi.mgd.api.model.voc.translator.AnnotationTranslator;
import org.jax.mgi.mgd.api.util.Constants;

public class AlleleTranslator extends BaseEntityDomainTranslator<Allele, AlleleDomain> {
	
	@Override
	protected AlleleDomain entityToDomain(Allele entity) {
		
		AlleleDomain domain = new AlleleDomain();
		
		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
		domain.setAlleleKey(String.valueOf(entity.get_allele_key()));
		domain.setSymbol(entity.getSymbol());
		domain.setName(entity.getName());
		domain.setIsWildType(entity.getIsWildType());
		domain.setIsExtinct(entity.getIsExtinct());
		domain.setIsMixed(entity.getIsMixed());
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));

		// primary mgi accession ids only
		if (!entity.getMgiAccessionIds().isEmpty()) {
			domain.setAccID(entity.getMgiAccessionIds().get(0).getAccID());
		}
		
		// marker stuff
		if (!entity.getMarker().getSymbol().isEmpty()) {
			domain.setMarkerKey(String.valueOf(entity.getMarker().get_marker_key()));
			domain.setMarkerSymbol(entity.getMarker().getSymbol());			
			domain.setChromosome(entity.getMarker().getChromosome());
			domain.setDetailClip(entity.getMarker().getDetailClipNote().get(0).getNote());
			// "strand" is not being translated; only used by 'allele/search'
			// only used in SlimAlleleDomain			
		}
		
		// reference associations
		if (!entity.getRefAssocs().isEmpty()) {
			MGIReferenceAssocTranslator refAssocTranslator = new MGIReferenceAssocTranslator();
			Iterable<MGIReferenceAssocDomain> i = refAssocTranslator.translateEntities(entity.getRefAssocs());
			domain.setRefAssocs(IteratorUtils.toList(i.iterator()));
		}	
		
		// mutations
		if (!entity.getMutations().isEmpty()) {
			AlleleMutationTranslator mutationTranslator = new AlleleMutationTranslator();
			Iterable<AlleleMutationDomain> i = mutationTranslator.translateEntities(entity.getMutations());
			domain.setMutations(IteratorUtils.toList(i.iterator()));
			domain.getMutations().sort(Comparator.comparing(AlleleMutationDomain::getMutation));
		}
		
		// subtype annotations
		if (!entity.getSubtypeAnnots().isEmpty()) {
			AnnotationTranslator annotTranslator = new AnnotationTranslator();
			Iterable<AnnotationDomain> i = annotTranslator.translateEntities(entity.getSubtypeAnnots());
			domain.setSubtypeAnnots(IteratorUtils.toList(i.iterator()));
		}
		
		// do annotations
		if (!entity.getDoAnnots().isEmpty()) {
			AnnotationTranslator annotTranslator = new AnnotationTranslator();
			Iterable<AnnotationDomain> i = annotTranslator.translateEntities(entity.getDoAnnots());
			domain.setDoAnnots(IteratorUtils.toList(i.iterator()));
		}
		
		return domain;
	}
	
}
