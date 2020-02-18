package org.jax.mgi.mgd.api.model.gxd.translator;

import java.util.Comparator;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.gxd.domain.AntibodyAliasDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.AntibodyDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.Antibody;
import org.jax.mgi.mgd.api.model.mgi.domain.MGIReferenceAssocDomain;
import org.jax.mgi.mgd.api.model.mgi.translator.MGIReferenceAssocTranslator;
import org.jax.mgi.mgd.api.model.mrk.domain.SlimMarkerDomain;
import org.jax.mgi.mgd.api.model.mrk.translator.SlimMarkerTranslator;
import org.jboss.logging.Logger;

public class AntibodyTranslator extends BaseEntityDomainTranslator<Antibody, AntibodyDomain> {

	protected Logger log = Logger.getLogger(getClass());

	private AntigenTranslator antigenTranslator = new AntigenTranslator();
	
	@Override
	protected AntibodyDomain entityToDomain(Antibody entity) {

		AntibodyDomain domain = new AntibodyDomain();

		domain.setAntibodyKey(String.valueOf(entity.get_antibody_key()));
		domain.setAntibodyName(entity.getAntibodyName());
		domain.setAntibodyNote(entity.getAntibodyNote());
		domain.setAntibodyClassKey(String.valueOf(entity.getAntibodyClass().get_antibodyclass_key()));
		domain.setAntibodyClass(entity.getAntibodyClass().getAntibodyClass());
		domain.setAntibodyTypeKey(String.valueOf(entity.getAntibodyType().get_antibodytype_key()));
		domain.setAntibodyType(entity.getAntibodyType().getAntibodyType());
		domain.setOrganismKey(String.valueOf(entity.getOrganism().get_organism_key()));
		domain.setOrganism(entity.getOrganism().getCommonname());		
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
		
		// mgi accession ids only
		if (entity.getMgiAccessionIds() != null && !entity.getMgiAccessionIds().isEmpty()) {
			domain.setAccID(entity.getMgiAccessionIds().get(0).getAccID());
		}

		// at most one probeSource
		if (entity.getAntigen() != null) {
			domain.setAntigen(antigenTranslator.translate(entity.getAntigen()));
		}
	
		// markers
		if (entity.getMarkers() != null && !entity.getMarkers().isEmpty()) {
			SlimMarkerTranslator markerTranslator = new SlimMarkerTranslator();			
			Iterable<SlimMarkerDomain> t = markerTranslator.translateEntities(entity.getMarkers());
			domain.setMarkers(IteratorUtils.toList(t.iterator()));
			domain.getMarkers().sort(Comparator.comparing(SlimMarkerDomain::getSymbol));
		}

		// reference associations
		if (entity.getRefAssocs() != null && !entity.getRefAssocs().isEmpty()) {
			MGIReferenceAssocTranslator refAssocTranslator = new MGIReferenceAssocTranslator();
			Iterable<MGIReferenceAssocDomain> i = refAssocTranslator.translateEntities(entity.getRefAssocs());
			domain.setRefAssocs(IteratorUtils.toList(i.iterator()));
			domain.getRefAssocs().sort(Comparator.comparing(MGIReferenceAssocDomain::getRefAssocType));
		}

		// aliases
		if (entity.getAliases() != null && !entity.getAliases().isEmpty()) {
			AntibodyAliasTranslator aliasTranslator = new AntibodyAliasTranslator();
			Iterable<AntibodyAliasDomain> i = aliasTranslator.translateEntities(entity.getAliases());
			domain.setAliases(IteratorUtils.toList(i.iterator()));
			domain.getAliases().sort(Comparator.comparing(AntibodyAliasDomain::getAlias));
		}
		
		return domain;
	}

}
