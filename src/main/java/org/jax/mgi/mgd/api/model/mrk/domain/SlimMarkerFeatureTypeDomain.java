package org.jax.mgi.mgd.api.model.mrk.domain;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.voc.domain.SlimTermDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SlimMarkerFeatureTypeDomain extends BaseDomain {
	
	// used by validateFeatureTypes service
	
	private String markerTypeKey;
	private List<SlimTermDomain> featureTypes;
  
}
