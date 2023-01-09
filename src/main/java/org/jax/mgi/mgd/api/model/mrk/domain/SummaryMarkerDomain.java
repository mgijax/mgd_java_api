package org.jax.mgi.mgd.api.model.mrk.domain;

import java.util.List;

import org.jax.mgi.mgd.api.model.BaseDomain;
import org.jax.mgi.mgd.api.model.mgi.domain.MGISynonymDomain;
import org.jax.mgi.mgd.api.model.voc.domain.MarkerFeatureTypeDomain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SummaryMarkerDomain extends BaseDomain {

	// a summary version of MarkerDomain 
	
	private String jnumID;
	private String markerKey;
	private String symbol;
	private String name;
    private String accID;
	private String markerStatusKey;	
	private String markerStatus;
	private String markerTypeKey;	
	private String markerType;
	private List<MGISynonymDomain> synonyms;
	private List<MarkerFeatureTypeDomain> featureTypes;
}
