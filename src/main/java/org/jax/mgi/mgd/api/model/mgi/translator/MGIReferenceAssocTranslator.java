package org.jax.mgi.mgd.api.model.mgi.translator;

import java.util.Comparator;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.all.domain.SlimAlleleRefAssocDomain;
import org.jax.mgi.mgd.api.model.all.translator.SlimAlleleRefAssocTranslator;
import org.jax.mgi.mgd.api.model.mgi.domain.MGIReferenceAssocDomain;
import org.jax.mgi.mgd.api.model.mgi.entities.MGIReferenceAssoc;
import org.jax.mgi.mgd.api.model.mrk.domain.SlimMarkerDomain;
import org.jax.mgi.mgd.api.model.mrk.translator.SlimMarkerTranslator;
import org.jax.mgi.mgd.api.util.Constants;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MGIReferenceAssocTranslator extends BaseEntityDomainTranslator<MGIReferenceAssoc, MGIReferenceAssocDomain> {

	@Override
	protected MGIReferenceAssocDomain entityToDomain(MGIReferenceAssoc entity) {
		MGIReferenceAssocDomain domain = new MGIReferenceAssocDomain();
		
		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
		domain.setAssocKey(String.valueOf(entity.get_assoc_key()));
		
		domain.setObjectKey(String.valueOf(entity.get_object_key()));
		domain.setMgiTypeKey(String.valueOf(entity.getMgiType().get_mgitype_key()));
		domain.setRefAssocTypeKey(String.valueOf(entity.getRefAssocType().get_refAssocType_key()));
	    domain.setRefAssocType(entity.getRefAssocType().getAssocType());
	    
		domain.setRefsKey(String.valueOf(entity.getReference().get_refs_key()));
		domain.setJnumid(entity.getReference().getReferenceCitationCache().getJnumid());
		domain.setJnum(String.valueOf(entity.getReference().getReferenceCitationCache().getNumericPart()));
		domain.setShort_citation(entity.getReference().getReferenceCitationCache().getShort_citation());
		
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));

		// one-to-many reference associations w/ allele info
		if (entity.getAlleles() != null && !entity.getAlleles().isEmpty()
				&& entity.getMgiType().get_mgitype_key() == 11) {
			SlimAlleleRefAssocTranslator alleleTranslator = new SlimAlleleRefAssocTranslator();
			Iterable<SlimAlleleRefAssocDomain> i = alleleTranslator.translateEntities(entity.getAlleles());
			domain.setAlleles(IteratorUtils.toList(i.iterator()));
			domain.getAlleles().sort(Comparator.comparing(SlimAlleleRefAssocDomain::getSymbol, String.CASE_INSENSITIVE_ORDER));
		}
		
		// one-to-many reference associations w/ marker info
		if (entity.getMarkers() != null && !entity.getMarkers().isEmpty()
				&& entity.getMgiType().get_mgitype_key() == 2) {
			SlimMarkerTranslator markerTranslator = new SlimMarkerTranslator();
			Iterable<SlimMarkerDomain> i = markerTranslator.translateEntities(entity.getMarkers());
			domain.setMarkers(IteratorUtils.toList(i.iterator()));
			domain.getMarkers().sort(Comparator.comparing(SlimMarkerDomain::getSymbol, String.CASE_INSENSITIVE_ORDER));
		}
				
		return domain;
	}

}
