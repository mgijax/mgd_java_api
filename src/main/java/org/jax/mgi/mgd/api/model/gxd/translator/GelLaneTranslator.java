package org.jax.mgi.mgd.api.model.gxd.translator;

import java.util.Comparator;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.gxd.domain.GelBandDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.GelLaneDomain;
import org.jax.mgi.mgd.api.model.gxd.domain.GelLaneStructureDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.GelLane;
import org.jax.mgi.mgd.api.util.Constants;
import org.jboss.logging.Logger;

public class GelLaneTranslator extends BaseEntityDomainTranslator<GelLane, GelLaneDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Override
	protected GelLaneDomain entityToDomain(GelLane entity) {

		GelLaneDomain domain = new GelLaneDomain();

		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
		domain.setGelLaneKey(String.valueOf(entity.get_gellane_key()));
		domain.setAssayKey(String.valueOf(entity.get_assay_key()));
		domain.setGenotypeKey(String.valueOf(entity.getGenotype().get_genotype_key()));
		domain.setGenotypeID(entity.getGenotype().getMgiAccessionIds().get(0).getAccID());
		domain.setGelRNATypeKey(String.valueOf(entity.getGelRNAType().get_gelrnatype_key()));
		domain.setGelRNAType(entity.getGelRNAType().getRnaType());
		domain.setGelControlKey(String.valueOf(entity.getGelControl().get_gelcontrol_key()));
		domain.setGelControl(entity.getGelControl().getGelLaneContent());
		domain.setSequenceNum(entity.getSequenceNum());
		domain.setLaneLabel(entity.getLaneLabel());
		domain.setSampleAmount(entity.getSampleAmount());
		domain.setSex(entity.getSex());
		domain.setAge(entity.getAge());
		domain.setAgeMin(String.valueOf(entity.getAgeMin()));
		domain.setAgeMax(String.valueOf(entity.getAgeMax()));
		domain.setAgeNote(entity.getAgeNote());
		domain.setLaneNote(entity.getLaneNote());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));

		// structures
		if (entity.getStructures() != null && !entity.getStructures().isEmpty()) {
			GelLaneStructureTranslator structureTranslator = new GelLaneStructureTranslator();
			Iterable<GelLaneStructureDomain> i = structureTranslator.translateEntities(entity.getStructures());
			domain.setStructures(IteratorUtils.toList(i.iterator()));
			domain.getStructures().sort(Comparator.comparing(GelLaneStructureDomain::getEmapaTerm));
		}

		// gel bands
		if (entity.getGelBands() != null && !entity.getGelBands().isEmpty()) {
			GelBandTranslator bandTranslator = new GelBandTranslator();
			Iterable<GelBandDomain> i = bandTranslator.translateEntities(entity.getGelBands());
			domain.setGelBands(IteratorUtils.toList(i.iterator()));
		}
		
		return domain;
	}

}
