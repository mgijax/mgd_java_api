package org.jax.mgi.mgd.api.model.mrk.domain;

import java.util.Map;

import org.jax.mgi.mgd.api.model.BaseDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MarkerEIUtilitiesDomain extends BaseDomain {

	private Map<String, String> eiUtilities;        
}
