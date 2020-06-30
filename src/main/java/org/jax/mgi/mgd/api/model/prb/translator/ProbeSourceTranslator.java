package org.jax.mgi.mgd.api.model.prb.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.prb.domain.ProbeSourceDomain;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeSource;
import org.jax.mgi.mgd.api.util.Constants;

public class ProbeSourceTranslator extends BaseEntityDomainTranslator<ProbeSource, ProbeSourceDomain> {

	@Override
	protected ProbeSourceDomain entityToDomain(ProbeSource entity) {
		
		ProbeSourceDomain domain = new ProbeSourceDomain();

		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);
		domain.setSourceKey(String.valueOf(entity.get_source_key()));
		domain.setName(entity.getName());
		domain.setDescription(entity.getDescription());
		domain.setAge(entity.getAge());
		domain.setSegmentTypeKey(String.valueOf(entity.getSegmentType().get_term_key()));
		domain.setSegmentType(entity.getSegmentType().getTerm());
		domain.setVectorKey(String.valueOf(entity.getVector().get_term_key()));
		domain.setVector(entity.getVector().getTerm());
		domain.setOrganismKey(String.valueOf(entity.getOrganism().get_organism_key()));
		domain.setOrganism(entity.getOrganism().getCommonname());
		domain.setStrainKey(String.valueOf(entity.getStrain().get_strain_key()));
		domain.setStrain(entity.getStrain().getStrain());
		domain.setTissueKey(String.valueOf(entity.getTissue().get_tissue_key()));
		domain.setTissue(entity.getTissue().getTissue());
		domain.setGenderKey(String.valueOf(entity.getGender().get_term_key()));
		domain.setGender(entity.getGender().getTerm());
		domain.setCellLineKey(String.valueOf(entity.getCellLine().get_term_key()));
		domain.setCellLine(entity.getCellLine().getTerm());	
		domain.setIsCuratorEdited(String.valueOf(entity.getIsCuratorEdited()));
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		domain.setModifiedByKey(entity.getModifiedBy().get_user_key().toString());
		domain.setModifiedBy(entity.getModifiedBy().getLogin());
		domain.setCreation_date(dateFormatNoTime.format(entity.getCreation_date()));
		domain.setModification_date(dateFormatNoTime.format(entity.getModification_date()));
		
		// reference can be null
		if (entity.getReference() != null) {
			domain.setRefsKey(String.valueOf(entity.getReference().get_refs_key()));
			domain.setJnumid(entity.getReference().getReferenceCitationCache().getJnumid());
			domain.setJnum(String.valueOf(entity.getReference().getReferenceCitationCache().getNumericPart()));
			domain.setShort_citation(entity.getReference().getReferenceCitationCache().getShort_citation());
		}
		
		return domain;
	}

}
