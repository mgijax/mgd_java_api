package org.jax.mgi.mgd.api.model.all.translator;

import java.util.Comparator;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.all.domain.AlleleFEARDomain;
import org.jax.mgi.mgd.api.model.all.entities.Allele;
import org.jax.mgi.mgd.api.model.mgi.domain.NoteDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.RelationshipFEARDomain;
import org.jax.mgi.mgd.api.model.mgi.translator.NoteTranslator;
import org.jax.mgi.mgd.api.model.mgi.translator.RelationshipFEARTranslator;
import org.jax.mgi.mgd.api.model.mrk.domain.GOTrackingDomain;
import org.jax.mgi.mgd.api.model.mrk.domain.MarkerAnnotDomain;
import org.jax.mgi.mgd.api.model.mrk.entities.Marker;
import org.jax.mgi.mgd.api.model.voc.domain.AnnotationDomain;
import org.jax.mgi.mgd.api.model.voc.translator.AnnotationTranslator;
import org.jboss.logging.Logger;

public class AlleleFEARTranslator extends BaseEntityDomainTranslator<Allele, AlleleFEARDomain> {

	protected Logger log = Logger.getLogger(getClass());

	@Override
	protected AlleleFEARDomain entityToDomain(Allele entity) {
		
		AlleleFEARDomain domain = new AlleleFEARDomain();

		// do not use 'processStatus' because this is a master domain
		// and only 1 master domain record is processed by the create/update endpoint
		
		domain.setAlleleKey(String.valueOf(entity.get_allele_key()));
		domain.setAlleleDisplay(entity.getSymbol() + ", " + entity.getName());
		domain.setSymbol(entity.getSymbol());
		
		// mgi accession ids only
		if (entity.getMgiAccessionIds() != null && !entity.getMgiAccessionIds().isEmpty()) {
			domain.setAccID(entity.getMgiAccessionIds().get(0).getAccID());
		}
		
		// relationship domain by allele
		// TO-DO/add List<RelationshipFEAR> to entity/Allele
//		if (entity.getRelationships() != null && !entity.getRelationships().isEmpty()) {
//			RelationshipFEARTranslator fearTranslator = new RelationshipFEARTranslator();	
//			Iterable<RelationshipFEARDomain> t = fearTranslator.translateEntities(entity.getRelationships());			
//			domain.setRelationships(IteratorUtils.toList(t.iterator()));
//			domain.getRelationships().sort(Comparator.comparing(RelationshipFEARDomain::getMarkerSymbol, String.CASE_INSENSITIVE_ORDER));	
//		}
		
		return domain;
	}

}
