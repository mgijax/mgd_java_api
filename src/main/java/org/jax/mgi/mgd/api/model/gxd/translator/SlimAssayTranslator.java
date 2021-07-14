package org.jax.mgi.mgd.api.model.gxd.translator;

import org.jax.mgi.mgd.api.model.BaseEntityDomainTranslator;
import org.jax.mgi.mgd.api.model.gxd.domain.SlimAssayDomain;
import org.jax.mgi.mgd.api.model.gxd.entities.Assay;

public class SlimAssayTranslator extends BaseEntityDomainTranslator<Assay, SlimAssayDomain> {
	
	@Override
	protected SlimAssayDomain entityToDomain(Assay entity) {
			
		SlimAssayDomain domain = new SlimAssayDomain();
		
		domain.setAssayKey(String.valueOf(entity.get_assay_key()));
		domain.setAssayTypeKey(String.valueOf(entity.getAssayType().get_assaytype_key()));
		domain.setAssayType(entity.getAssayType().getAssayType());

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
		}
		else if (entity.getAssayType().get_assaytype_key() == 2) {
			domain.setAssayTypeAbbrev("North");
		}
		else if (entity.getAssayType().get_assaytype_key() == 3) {
			domain.setAssayTypeAbbrev("S1Nuc");
		}
		else if (entity.getAssayType().get_assaytype_key() == 4) {
			domain.setAssayTypeAbbrev("RNAse");
		}
		else if (entity.getAssayType().get_assaytype_key() == 5) {
			domain.setAssayTypeAbbrev("RTPCR");
		}
		else if (entity.getAssayType().get_assaytype_key() == 6) {
			domain.setAssayTypeAbbrev("Immuno");
		}
		else if (entity.getAssayType().get_assaytype_key() == 8) {
			domain.setAssayTypeAbbrev("West");
		}
		else if (entity.getAssayType().get_assaytype_key() == 9) {
			domain.setAssayTypeAbbrev("Knock");
		}
		else if (entity.getAssayType().get_assaytype_key() == 10) {
			domain.setAssayTypeAbbrev("Transg");
		}
		else if (entity.getAssayType().get_assaytype_key() == 11) {
			domain.setAssayTypeAbbrev("Recomb");
		}
		
		domain.setAssayDisplay(entity.getReference().getReferenceCitationCache().getJnumid() + ";" + domain.getAssayTypeAbbrev() + ";" + entity.getMarker().getSymbol());	

		domain.setRefsKey(String.valueOf(entity.getReference().get_refs_key()));
		domain.setJnumid(entity.getReference().getReferenceCitationCache().getJnumid());
		domain.setJnum(String.valueOf(entity.getReference().getReferenceCitationCache().getNumericPart()));
		domain.setCreatedByKey(entity.getCreatedBy().get_user_key().toString());
		domain.setCreatedBy(entity.getCreatedBy().getLogin());
		
		return domain;
	}

}
