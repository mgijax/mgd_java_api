package org.jax.mgi.mgd.api.model.mrk.domain;

import java.util.Map;

import org.jax.mgi.mgd.api.model.BaseDomain;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@ApiModel(value = "Marker EI Utilities Domain Object")
public class MarkerEIUtilitiesDomain extends BaseDomain {

	private Map<String, String> eiUtilities;        
}
