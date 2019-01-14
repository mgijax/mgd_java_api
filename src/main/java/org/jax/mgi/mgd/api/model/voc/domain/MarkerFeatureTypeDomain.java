package org.jax.mgi.mgd.api.model.voc.domain;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.acc.domain.SlimAccessionDomain;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@ApiModel(value = "Marker Feature Type Domain")
public class MarkerFeatureTypeDomain extends BaseDomain {

	private String processStatus;
	private String annotKey;	
	private String termKey;
	private String term;
	private List<SlimAccessionDomain> markerFeatureTypeIds;
	
}
