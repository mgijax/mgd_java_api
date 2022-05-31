package org.jax.mgi.mgd.api.model.gxd.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.gxd.domain.SlimAssayDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.Assay;

public class SlimAssayTranslator extends BaseEntityDomainTranslator<Assay, SlimAssayDomain> {
	
	@Override
	protected SlimAssayDomain entityToDomain(Assay entity) {
			
		SlimAssayDomain domain = new SlimAssayDomain();
		
		domain.setAssayKey(String.valueOf(entity.get_assay_key()));
		domain.setAccID(entity.getMgiAccessionIds().get(0).getAccID());
		domain.setAssayTypeKey(String.valueOf(entity.getAssayType().get_assaytype_key()));
		domain.setAssayType(entity.getAssayType().getAssayType());
		domain.setMarkerKey(String.valueOf(entity.getMarker().get_marker_key()));
		domain.setMarkerSymbol(entity.getMarker().getSymbol());
		domain.setMarkerAccID(entity.getMarker().getMgiAccessionIds().get(0).getAccID());
		
//        1 InSitu | RNA in situ                    
//        2 North  | Northern blot              
//        3 S1Nuc  | Nuclease S1                 
//        4 RNAse  | RNase protection      
//        5 RTPCR  | RT-PCR                           
//        6 Immuno | Immunohistochemistry          
//        8 West   | Western blot               
//        9 Knock  | In situ reporter (knock in)       
//       10 Transg | In situ reporter (transgenic)                 
//       11 Recomb | Recombinase reporter     
		
		if (entity.getAssayType().get_assaytype_key() == 1) {
			domain.setAssayTypeAbbrev("InSitu");
			domain.setIsInSitu(true);
			domain.setIsProbePrep(true);
		}
		else if (entity.getAssayType().get_assaytype_key() == 2) {
			domain.setAssayTypeAbbrev("North");
			domain.setIsGel(true);
			domain.setIsProbePrep(true);		
		}
		else if (entity.getAssayType().get_assaytype_key() == 3) {
			domain.setAssayTypeAbbrev("S1Nuc");
			domain.setIsGel(true);	
			domain.setIsProbePrep(true);			
		}
		else if (entity.getAssayType().get_assaytype_key() == 4) {
			domain.setAssayTypeAbbrev("RNAse");
			domain.setIsGel(true);		
			domain.setIsProbePrep(true);		
		}
		else if (entity.getAssayType().get_assaytype_key() == 5) {
			domain.setAssayTypeAbbrev("RTPCR");
			domain.setIsGel(true);		
			domain.setIsProbePrep(true);			
		}
		else if (entity.getAssayType().get_assaytype_key() == 6) {
			domain.setAssayTypeAbbrev("Immuno");
			domain.setIsInSitu(true);		
			domain.setIsAntibodyPrep(true);
		}
		else if (entity.getAssayType().get_assaytype_key() == 8) {
			domain.setAssayTypeAbbrev("West");
			domain.setIsGel(true);	
			domain.setIsAntibodyPrep(true);
		}
		else if (entity.getAssayType().get_assaytype_key() == 9) {
			domain.setAssayTypeAbbrev("Knock");
			domain.setIsInSitu(true);	
			domain.setIsReporter(true);
		}
		else if (entity.getAssayType().get_assaytype_key() == 10) {
			domain.setAssayTypeAbbrev("Transg");
			domain.setIsInSitu(true);	
			domain.setIsReporter(true);			
		}
		else if (entity.getAssayType().get_assaytype_key() == 11) {
			domain.setAssayTypeAbbrev("Recomb");
			domain.setIsInSitu(true);
			domain.setIsReporter(true);			
		}
		
		// too slow; use service/return for references
//		domain.setAssayDisplay(entity.getReference().getReferenceCitationCache().getJnumid() + "; " + domain.getAssayTypeAbbrev() + "; " + entity.getMarker().getSymbol());	
//		domain.setRefsKey(String.valueOf(entity.getReference().get_refs_key()));
//		domain.setJnumid(entity.getReference().getReferenceCitationCache().getJnumid());
//		domain.setJnum(String.valueOf(entity.getReference().getReferenceCitationCache().getNumericPart()));

		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		
		return domain;
	}

}
