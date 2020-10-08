package org.jax.mgi.mgd.api.model.prb.domain;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SlimProbeSourceDomain extends BaseDomain {

	private String sourceKey;
	private String name;
	private String description;
	private String age;
	private String agePrefix;
	private String ageStage;
	private String organismKey;
	private String organism;
	private String strainKey;
	private String strain;
	private String tissueKey;
	private String tissue;
	private String genderKey;
	private String gender;
	private String cellLineKey;
	private String cellLine;		
}
