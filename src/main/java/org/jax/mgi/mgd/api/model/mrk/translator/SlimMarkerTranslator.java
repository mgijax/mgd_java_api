package org.jax.mgi.mgd.api.model.mrk.translator;

import java.util.Comparator;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.acc.domain.AccessionDomain;
import org.jax.mgi.mgd.api.model.acc.translator.AccessionTranslator;
import org.jax.mgi.mgd.api.model.mgi.domain.RelationshipMarkerTSSDomain;
import org.jax.mgi.mgd.api.model.mrk.domain.MarkerNoteDomain;
import org.jax.mgi.mgd.api.model.mrk.domain.SlimMarkerDomain;
import org.jax.mgi.mgd.api.model.mrk.entities.Marker;
import org.jboss.logging.Logger;

public class SlimMarkerTranslator extends BaseEntityDomainTranslator<Marker, SlimMarkerDomain> {

	protected Logger log = Logger.getLogger(getClass());
	private AccessionTranslator accessionTranslator = new AccessionTranslator();

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
		else {
			domain.setAccID("");
		}

		// accession ids editable for mouse
		if (domain.getAccID().isEmpty() && entity.getOrganism().get_organism_key() != 1 && entity.getEditAccessionIdsNonMouse() != null && !entity.getEditAccessionIdsNonMouse().isEmpty()) {
			Iterable<AccessionDomain> acc = accessionTranslator.translateEntities(entity.getEditAccessionIdsNonMouse());
			for (AccessionDomain i : acc) {
				if (i.getLogicaldbKey().equals("55")) {
					domain.setAccID(i.getAccID());
				}
			}
		}
		
		// accession ids non-editable for mouse
		if (domain.getAccID().isEmpty() && entity.getOrganism().get_organism_key() != 1 && entity.getNonEditAccessionIdsNonMouse() != null && !entity.getEditAccessionIdsNonMouse().isEmpty()) {
			Iterable<AccessionDomain> acc = accessionTranslator.translateEntities(entity.getNonEditAccessionIdsNonMouse());
			for (AccessionDomain i : acc) {
				if (i.getLogicaldbKey().equals("55")) {
					domain.setAccID(i.getAccID());
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
