package org.jax.mgi.mgd.api.model.mrk.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.mrk.domain.MarkerNoteDomain;
import org.jax.mgi.mgd.api.model.mrk.domain.SlimMarkerDomain;
import org.jax.mgi.mgd.api.model.mrk.entities.Marker;
import org.jboss.logging.Logger;

public class SlimMarkerTranslator extends BaseEntityDomainTranslator<Marker, SlimMarkerDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Override
	protected SlimMarkerDomain entityToDomain(Marker entity) {
			
		SlimMarkerDomain domain = new SlimMarkerDomain();
		
		domain.setMarkerKey(String.valueOf(entity.get_marker_key()));
		domain.setSymbol(entity.getSymbol());
		domain.setName(entity.getName());
		domain.setChromosome(entity.getChromosome());
		domain.setOrganismKey(String.valueOf(entity.getOrganism().get_organism_key()));
		domain.setOrganism(entity.getOrganism().getCommonname());
		domain.setOrganismLatin(entity.getOrganism().getLatinname());
		domain.setMarkerStatusKey(entity.getMarkerStatus().get_marker_status_key().toString());
		domain.setMarkerStatus(entity.getMarkerStatus().getStatus());
		domain.setMarkerTypeKey(entity.getMarkerType().get_marker_type_key().toString());
		domain.setMarkerType(entity.getMarkerType().getName());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());

		if (entity.getMgiAccessionIds() != null && !entity.getMgiAccessionIds().isEmpty()) {
			domain.setAccID(entity.getMgiAccessionIds().get(0).getAccID());
		}
		// determine primary accession id to return per organism
		// _organism_key = 2, human -> _logicaldb_key = 55, Entrez Gene
		else if (entity.getOrganism().get_organism_key() == 2 && entity.getNonEditAccessionIds() != null){
			for (int i = 0; i < entity.getNonEditAccessionIds().size(); i++) {
				if (entity.getNonEditAccessionIds().get(i).getLogicaldb().get_logicaldb_key() == 55) {
					domain.setAccID(entity.getNonEditAccessionIds().get(i).getAccID());
				}
			}
		}

		// marker detail clip
		if (entity.getDetailClipNote() != null && !entity.getDetailClipNote().isEmpty()) {
			MarkerNoteTranslator noteTranslator = new MarkerNoteTranslator();
			Iterable<MarkerNoteDomain> note = noteTranslator.translateEntities(entity.getDetailClipNote());
			domain.setDetailClip(note.iterator().next());
		}
				
		return domain;
	}

}
