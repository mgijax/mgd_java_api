package org.jax.mgi.mgd.api.model.gxd.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.gxd.domain.GelLaneDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.GelLane;

public class GelLaneTranslator extends BaseEntityDomainTranslator<GelLane, GelLaneDomain> {

	@Override
	protected GelLaneDomain entityToDomain(GelLane entity, int translationDepth) {
		
		GelLaneDomain domain = new GelLaneDomain();
		domain.set_gellane_key(entity.get_gellane_key());
		domain.setSequenceNum(entity.getSequenceNum());
		domain.setLaneLabel(entity.getLaneLabel());
		domain.setSampleAmount(entity.getSampleAmount());
		domain.setSex(entity.getSex());
		domain.setAge(entity.getAge());
		domain.setAgeMin(entity.getAgeMin());
		domain.setAgeMax(entity.getAgeMax());
		domain.setAgeNote(entity.getAgeNote());
		domain.setLaneNote(entity.getLaneNote());
		domain.setCreation_date(entity.getCreation_date());
		domain.setModification_date(entity.getModification_date());
		
		if(translationDepth > 0) {	
		}
		return domain;
	}

	@Override
	protected GelLane domainToEntity(GelLaneDomain domain, int translationDepth) {
		// TODO Auto-generated method stub
		return null;
	}

}
