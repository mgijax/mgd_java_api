package org.jax.mgi.mgd.api.model.prb.translator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections4.IteratorUtils;
import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.acc.domain.AccessionDomain;
import org.jax.mgi.mgd.api.model.acc.translator.AccessionTranslator;
import org.jax.mgi.mgd.api.model.prb.domain.ProbeSourceDomain;
import org.jax.mgi.mgd.api.model.prb.entities.ProbeSource;
import org.jax.mgi.mgd.api.util.Constants;
import org.jboss.logging.Logger;

public class ProbeSourceTranslator extends BaseEntityDomainTranslator<ProbeSource, ProbeSourceDomain> {

	protected Logger log = Logger.getLogger(getClass());
	
	@Override
	protected ProbeSourceDomain entityToDomain(ProbeSource entity) {
		
		ProbeSourceDomain domain = new ProbeSourceDomain();

		domain.setProcessStatus(Constants.PROCESS_NOTDIRTY);		
		domain.setSourceKey(String.valueOf(entity.get_source_key()));
		domain.setName(entity.getName());
		domain.setDescription(entity.getDescription());
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
		
		// age stuff
		
		domain.setAge(entity.getAge());
		String age = domain.getAge();
		
		if (age.equals("Not Applicable")
				|| age.equals("Not Loaded")
				|| age.equals("Not Resolved")
				|| age.equals("Not Specified")
				|| age.equals("embryonic")
				|| age.equals("embryonic brain")
				|| age.equals("postnatal")
				|| age.equals("postnatal adult")
				|| age.equals("postnatal newborn")
				) {
			domain.setAgePrefix(age);
		}
		else {		
			// example :  embryonic day 13.5,14.5,16.5,17.5
			// agePrefix = "embryonic day" : list[0], list[1]
			// ageStage = "13.5,14.5,16.5,17.5" : remainder of list
			List<String> ageList = new ArrayList<String>(Arrays.asList(age.split(" ")));
			domain.setAgePrefix(ageList.get(0) + " " + ageList.get(1));
			String ageStage = "";
			for (int i = 2; i < ageList.size(); i++) {
				ageStage = ageStage + ageList.get(i);
			}
			//log.info("PS Translator ageStage: " + ageStage); 
			domain.setAgeStage(ageStage);			
		}
		
		// end age stuff
		
		// reference can be null
		if (entity.getReference() != null) {
			domain.setRefsKey(String.valueOf(entity.getReference().get_refs_key()));
			domain.setJnumid(entity.getReference().getJnumid());
			domain.setJnum(String.valueOf(entity.getReference().getNumericPart()));
			domain.setShort_citation(entity.getReference().getShort_citation());
		}

		//  accession ids 
		if (entity.getAccessionIds() != null && !entity.getAccessionIds().isEmpty()) {
			AccessionTranslator accessionTranslator = new AccessionTranslator();			
			Iterable<AccessionDomain> acc = accessionTranslator.translateEntities(entity.getAccessionIds());
			domain.setAccessionIds(IteratorUtils.toList(acc.iterator()));
		}
		
		return domain;
	}

}
