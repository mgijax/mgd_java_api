package org.jax.mgi.mgd.api.model.voc.domain;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.acc.domain.SlimAccessionDomain;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@ApiModel(value = "Annotation Domain")
public class AnnotationDomain extends BaseDomain {

	private String processStatus;
	private String annotKey;
	private String annotTypeKey;
	private String annotType;
	private String objectKey;
	private String termKey;
	private String term;
	private String goDagAbbrev;
	private String qualifierKey;
	private String qualifierAbbreviation;
	private String qualifier;
	private String creation_date;
	private String modification_date;
	
	private List<EvidenceDomain> evidence;
	
	private List<SlimAccessionDomain> markerFeatureTypes;
	private List<SlimAccessionDomain> alleleVariantSOIds;
	private List<SlimAccessionDomain> mpIds;
	private List<SlimAccessionDomain> doIds;
	private List<SlimAccessionDomain> goIds;

	private Boolean allowEditTerm = true;
	
}
