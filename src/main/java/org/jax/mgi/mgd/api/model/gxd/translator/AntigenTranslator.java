package org.jax.mgi.mgd.api.model.gxd.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.gxd.domain.AntigenDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.Antigen;
import org.jax.mgi.mgd.api.model.prb.translator.ProbeSourceTranslator;
import org.jboss.logging.Logger;

public class AntigenTranslator extends BaseEntityDomainTranslator<Antigen, AntigenDomain> {

	protected Logger log = Logger.getLogger(getClass());

	private ProbeSourceTranslator probeSourceTranslator = new ProbeSourceTranslator();
	
	@Override
	protected AntigenDomain entityToDomain(Antigen entity) {

		AntigenDomain domain = new AntigenDomain();

		domain.setAntigenKey(String.valueOf(entity.get_antigen_key()));
		domain.setAntigenName(entity.getAntigenName());
		domain.setRegionCovered(entity.getRegionCovered());
		domain.setAntigenNote(entity.getAntigenNote());		
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
		if (entity.getProbeSource() != null) {
			domain.setProbeSource(probeSourceTranslator.translate(entity.getProbeSource()));
		}

		return domain;
	}

}
